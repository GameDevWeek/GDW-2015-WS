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
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.events.BubblegumSpitSpawnEvent;
import de.hochschuletrier.gdw.ws1516.events.BulletSpawnEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.BubbleGlueComponent;
import de.hochschuletrier.gdw.ws1516.game.components.BubblegumSpitComponent;
import de.hochschuletrier.gdw.ws1516.game.components.BulletComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;

public class BulletSystem extends IteratingSystem implements BulletSpawnEvent.Listener {
    private static final Logger logger = LoggerFactory.getLogger(BubblegumSpitSystem.class);
    private PooledEngine engine;

    public BulletSystem(PooledEngine engine) {
        super(Family.all(BulletComponent.class,
                         PositionComponent.class,
                         PhysixBodyComponent.class).get());
        this.engine = engine;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        BulletSpawnEvent.register(this);
    }   
    
    @Override
    public void removedFromEngine (Engine engine) {
        super.removedFromEngine(engine);
        BulletSpawnEvent.unregister(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        PhysixBodyComponent body = ComponentMappers.physixBody.get(entity);
        
        if (position != null && body != null) { 
            PhysixModifierComponent modifier = engine.createComponent(PhysixModifierComponent.class);
            modifier.schedule(() -> {
                body.setAngle(body.getLinearVelocity().angle() * PhysixUtil.DEG2RAD);
            });
            entity.add(modifier);
        }
    }
    
    @Override
    public void onSpawnBullet (final float spawnX, final float spawnY, final float directionX, final float directionY, 
                               final BiConsumer<Entity, Entity> onPlayerHit,
                               final BiConsumer<Entity, Entity> onEntityHit,
                               final Consumer<Entity> onHit) {

        //Create bullet entity
        Entity bulletEntity = EntityCreator.createEntity("bullet", spawnX, spawnY);
        
        //Set callback's
        BulletComponent bulletComponent = ComponentMappers.bulletComponent.get(bulletEntity);
        bulletComponent.onPlayerHit = onPlayerHit;
        bulletComponent.onEntityHit = onEntityHit;
        bulletComponent.onHit = onHit;
        
        //Add body manually
        PhysixModifierComponent phyModifier = engine.createComponent(PhysixModifierComponent.class);
        phyModifier.schedule(() -> {
            
            //Get physix system
            PhysixSystem physix = engine.getSystem(PhysixSystem.class);
            
            //Normalize direction
            float directionLen = (float) Math.sqrt(directionX * directionX + directionY * directionY);
            float nDirX = directionX / directionLen;
            float nDirY = directionY / directionLen;
            
            //Create physic Body component
            PhysixBodyComponent bulletBodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bulletBodyDef = new PhysixBodyDef(BodyType.DynamicBody, physix)
                                            .position(spawnX, spawnY)
                                            .fixedRotation(true);
            bulletBodyComponent.init(bulletBodyDef, physix, bulletEntity);
            
            //Bullet fixture
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physix)
                                          .density(1.0f)
                                          .friction(0.0f)
                                          .restitution(0.0f)
                                          .shapeCircle(5);
            bulletBodyComponent.createFixture(fixtureDef);
            
            //Force bullet
            bulletBodyComponent.applyImpulse(nDirX * GameConstants.BULLET_SPEED, nDirY * GameConstants.BULLET_SPEED);
            bulletBodyComponent.setGravityScale(0.0f);
            
            //Add body to entity
            bulletEntity.add(bulletBodyComponent);
           
        });
        bulletEntity.add(phyModifier);
        
    }
    
}

