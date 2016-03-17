package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.events.BubblegumSpitSpawnEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.BubbleGlueComponent;
import de.hochschuletrier.gdw.ws1516.game.components.BubblegumSpitComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;

/**
 * Handles bubble-gum spits
 * @author Eileen
 * @version 1.0
 */
public class BubblegumSpitSystem extends EntitySystem implements BubblegumSpitSpawnEvent.Listener {
    private static final Logger logger = LoggerFactory.getLogger(BubblegumSpitSystem.class);

    private PooledEngine engine;
    
    public BubblegumSpitSystem(PooledEngine e) {
        super(0);
        this.engine = e;
    }

    public BubblegumSpitSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        BubblegumSpitSpawnEvent.register(this);
    }   
    
    @Override
    public void removedFromEngine (Engine engine) {
        BubblegumSpitSpawnEvent.unregister(this);
    }

    @Override
    public void onSpawnBubblegumSpit(final float force) {
        
        //Get player entity
        Entity playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                                                               MovementComponent.class).get()).first();
        PhysixBodyComponent playerBody   = ComponentMappers.physixBody.get(playerEntity);
        MovementComponent playerMovement = ComponentMappers.movement.get(playerEntity);
        
        //Player view direction cosine
        float playerViewCosine = 1.0f;
        if (playerMovement.lookDirection == MovementComponent.LookDirection.LEFT)
            playerViewCosine = -1.0f;
        
        //Compute spawn position
        float spawnX = playerBody.getX() + (GameConstants.SPIT_SPAWN_OFFSET_X * playerViewCosine);
        float spawnY = playerBody.getY() +  GameConstants.SPIT_SPAWN_OFFSET_Y;

        //Create gum spit entity at player position
        final Entity gumSpitEntity = EntityCreator.createEntity("bubblegum_spitting", spawnX, spawnY);
        
        new Thread(() -> {
            
            while (ComponentMappers.physixBody.get(gumSpitEntity) == null) { }
            
            logger.debug("now there is a body");
            
        }).start();
        
        ComponentMappers.physixModifier.get(gumSpitEntity)
                                       .schedule(() -> {
                                           logger.debug("schedule {}", ComponentMappers.physixBody.get(gumSpitEntity));
                                       });
        

        
        engine.addEntity(gumSpitEntity);
        
    }
    
    private void makeBubbleGlue(Entity gum, Entity enemy) {
        
        //Fetch body
        PhysixBodyComponent enemyBody = ComponentMapper.getFor(PhysixBodyComponent.class).get(enemy);
        
        //Create glue entity
        final Entity glueEntity = engine.createEntity();
        
        BubbleGlueComponent glueComponent = engine.createComponent(BubbleGlueComponent.class);
        glueComponent.gluedEntity = enemy;
        glueComponent.gluedToPosition = new Vector2(enemyBody.getX(), enemyBody.getY());
        glueComponent.timeRemaining = GameConstants.SPIT_GLUE_COOLDOWN;
        
        glueEntity.add(glueComponent);
        
        engine.addEntity(glueEntity);
        
    }
    
    private void removeBubblegumSpit(Entity gum) {
        logger.debug("removed");
        engine.removeEntity(gum);
    }
    
}
