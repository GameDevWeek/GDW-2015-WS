package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityInfo;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent;
import de.hochschuletrier.gdw.ws1516.events.TriggerEvent;
import de.hochschuletrier.gdw.ws1516.events.TriggerEvent.Action;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.HitPointsComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;
import de.hochschuletrier.gdw.ws1516.sandbox.maptest.MapTest;

public class RespawnSystem extends IteratingSystem implements GameRespawnEvent.Listener, EntityListener {

    private static final Logger logger = LoggerFactory.getLogger(GameLogicTest.class);
    private Entity player;
    
    public RespawnSystem()
    {
        super(Family.all(PlayerComponent.class).get());
    }
    
   @Override
    protected void processEntity(Entity entity, float deltaTime) {

       PlayerComponent playerComp = ComponentMappers.player.get(player);
       PhysixBodyComponent physixBody = ComponentMappers.physixBody.get(entity);
       StartPointComponent respawnPosition = ComponentMappers.startPoint.get(entity);
       HitPointsComponent hitPoints = ComponentMappers.hp.get(entity);
       if ( physixBody != null && playerComp.doRespawn)
       {
           physixBody.setPosition(respawnPosition.x,respawnPosition.y);
           hitPoints.value = hitPoints.max;
           playerComp.doRespawn = false;
       }
    }
    
    @Override
    public void onGameRepawnEvent() {
        PlayerComponent playerComp = ComponentMappers.player.get(player);
        if ( playerComp != null )
        {
            playerComp.doRespawn = true;
        }else
        {
            logger.warn("No Player or no RespawnPoint set");
        }
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        GameRespawnEvent.unregister(this);
        engine.removeEntityListener(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
       // Family family = Family.one(PlayerComponent.class, StartPointComponent.class).get();
        super.addedToEngine(engine);
        engine.addEntityListener(this);
        GameRespawnEvent.register(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        if (ComponentMappers.player.get(entity) != null ) {
            player = entity;
            StartPointComponent start = player.getComponent(StartPointComponent.class );
            PositionComponent position = player.getComponent(PositionComponent.class );
            start.x = position.x;
            start.y = position.y;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (entity == player) {
            player = null;
        } 
    }

}
