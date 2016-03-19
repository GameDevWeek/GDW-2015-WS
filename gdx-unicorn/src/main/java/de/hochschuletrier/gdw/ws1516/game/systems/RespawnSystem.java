package de.hochschuletrier.gdw.ws1516.game.systems;

import java.rmi.activation.ActivationSystem;
import java.util.Iterator;

import org.slf4j.Logger;
import de.hochschuletrier.gdw.ws1516.events.ActivateSafePointEvent;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.ActivateSafePointEvent;
import de.hochschuletrier.gdw.ws1516.events.ActivateSafePointEvent.Listener;
import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.CollectableComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent.SavedEntities;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;

public class RespawnSystem extends IteratingSystem implements GameRespawnEvent.Listener, EntityListener, ActivateSafePointEvent.Listener  {

    private static final Logger logger = LoggerFactory.getLogger(GameLogicTest.class);
    private Entity player;
    private Engine engine;
    private boolean initStartSpawn = false;
    
    public RespawnSystem()
    {
        super(Family.all(PlayerComponent.class).get());
    }
    
   @Override
    protected void processEntity(Entity entity, float deltaTime) {

       PlayerComponent playerComp = ComponentMappers.player.get(entity);
       PhysixBodyComponent physixBody = ComponentMappers.physixBody.get(entity);
       StartPointComponent respawnPosition = ComponentMappers.startPoint.get(entity);
       if ( physixBody != null && playerComp.doRespawn)
       {
           physixBody.setPosition(respawnPosition.x,respawnPosition.y);
           playerComp.blueGumStacks = respawnPosition.blueGums;
           playerComp.hitpoints = playerComp.maxHitpoints;
           playerComp.doRespawn = false;
           /// Welt zurücksetzten
           for (SavedEntities save : respawnPosition.savedEntities )
           {
               if ( save.saved != null )
               {
                   PhysixBodyComponent saveBody = ComponentMappers.physixBody.get(save.saved);
                   PositionComponent savePos = ComponentMappers.position.get(save.saved);
                   if ( saveBody != null && save.position!= null )
                   {
                       saveBody.setPosition(save.position.x, save.position.y);
                   }
                   logger.debug("X : {}, Y : {}",save.position.x,save.position.y);
               } else {
                   revive(save);
               }
           }
       }
    }
    
    private void revive(SavedEntities save) {
        EntityCreator.createEntity(save.entityType.entityName().toLowerCase(), save.position.x, save.position.y);
    }

    @Override
    public void onGameRepawnEvent() {
        PlayerComponent playerComp = ComponentMappers.player.get(player);
        MovementComponent move= ComponentMappers.movement.get(player);
        if ( playerComp != null )
        {
            playerComp.doRespawn = true;
            playerComp.invulnerableTimer=1.0f;
            if ( move != null )
            {
                move.reset();
            }
            
        }else
        {
            logger.warn("No Player or no RespawnPoint set");
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
       // Family family = Family.one(PlayerComponent.class, StartPointComponent.class).get();
        super.addedToEngine(engine);
        engine.addEntityListener(this);
        GameRespawnEvent.register(this);
        ActivateSafePointEvent.register(this);
        this.engine = engine;
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        GameRespawnEvent.unregister(this);
        ActivateSafePointEvent.unregister(this);
        engine.removeEntityListener(this);
        this.engine = null;
    }

    @Override
    public void entityAdded(Entity entity) {
        if (ComponentMappers.player.get(entity) != null ) 
        {
            player = entity;
        }
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if ( !initStartSpawn  )
        {
            onActivateCheckPointEvent(player, player);
            initStartSpawn = true;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (entity == player) {
            player = null;
        } else
        {   /// check if an saved enemy died 
            StartPointComponent start = ComponentMappers.startPoint.get(player);
            Iterator<SavedEntities> iter = start.savedEntities.iterator();
            while ( iter.hasNext() )
            {
                SavedEntities save = iter.next();
                /// Entity no longer exist
                save.saved = null;
            }
        }
        
    }

    @Override
    public void onActivateCheckPointEvent(Entity unicorn, Entity safePoint) {
        PositionComponent safePosition = ComponentMappers.position.get(safePoint);
        PlayerComponent player = ComponentMappers.player.get(unicorn);
        StartPointComponent start = ComponentMappers.startPoint.get(unicorn);
        // alle entity (die ich brauch ) merken
        if ( start != null )
        {
            start.savedEntities.clear();
            
            ImmutableArray<Entity> eList = engine.getEntitiesFor(Family.all(PositionComponent.class).exclude(PlayerComponent.class).get());
            for( Entity e:eList)
            {
                CollectableComponent collect = ComponentMappers.collectable.get(e);
                
                if( collect == null || ( collect.type != CollectableComponent.CollectableType.CHOCO_COIN &&
                    collect.type != CollectableComponent.CollectableType.BONBON ) )
                {
                    start.savedEntities.add(new SavedEntities(e));
                }
            }
            start.x = safePosition.x;
            start.y = safePosition.y;
            start.blueGums = player.blueGumStacks;
        }
        
    }

}
