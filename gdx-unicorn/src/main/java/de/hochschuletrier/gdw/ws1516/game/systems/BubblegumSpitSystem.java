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
        
        //Create gum spit entity
        final Entity gumSpitEntity = engine.createEntity();
        
        //Create gum spit component
        BubblegumSpitComponent bulletComponent = engine.createComponent(BubblegumSpitComponent.class);
        bulletComponent.onEnemyHit = this::makeBubbleGlue;
        bulletComponent.onHit = this::removeBubblegumSpit;
        
        //Create physics modifier component
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        
        //Bind components
        gumSpitEntity.add(bulletComponent);
        gumSpitEntity.add(modifyComponent);

        //Build bullet physics
        modifyComponent.schedule(() -> {
            PhysixSystem physixSystem = engine.getSystem(PhysixSystem.class);
            
            Entity playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                                                                   MovementComponent.class).get()).first();
            PhysixBodyComponent playerBody = ComponentMappers.physixBody.get(playerEntity);
            MovementComponent movementComponent = ComponentMappers.movement.get(playerEntity);
            
            //'Magie = Physik / Wollen'
            float lookDirection = movementComponent.lookDirection == MovementComponent.LookDirection.RIGHT ? 1.0f : -1.0f;
            float spawnX = playerBody.getX() + (GameConstants.SPIT_SPAWN_OFFSET_X * lookDirection);
            float spawnY = playerBody.getY() + GameConstants.SPIT_SPAWN_OFFSET_Y;
            float impulseX = (float) Math.cos(GameConstants.SPIT_SPAWN_ANGLE) * lookDirection;
            float impulseY = (float) Math.sin(GameConstants.SPIT_SPAWN_ANGLE);
            
            //Create physic Body component
            PhysixBodyComponent spitBodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef spitBodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem)
                                            .position(spawnX, spawnY)
                                            .fixedRotation(true);
            spitBodyComponent.init(spitBodyDef, physixSystem, gumSpitEntity);
            
            //Gum spit fixture
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                                          .density(1.0f)
                                          .friction(0.0f)
                                          .restitution(0.0f)
                                          .shapeCircle(5);
            fixtureDef.filter.groupIndex = GameConstants.PHYSIX_COLLISION_SPIT;
            spitBodyComponent.createFixture(fixtureDef);
            
            //Force gum spit
            float spitForceDelta = GameConstants.SPIT_FORCE_MAX - GameConstants.SPIT_FORCE_MIN;
            float spitForce = GameConstants.SPIT_FORCE_MIN + force * spitForceDelta;
            spitBodyComponent.applyImpulse(impulseX * spitForce, impulseY * spitForce);
            spitBodyComponent.setGravityScale(1.0f);
            
            //Add body to entity
            gumSpitEntity.add(spitBodyComponent);
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
        engine.removeEntity(gum);
    }
    
}
