package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.events.BulletSpawnEvent;
import de.hochschuletrier.gdw.ws1516.game.components.BulletComponent;

public class BulletSystem extends EntitySystem implements BulletSpawnEvent.Listener {

    /** Speed of a bullet in worldUnits / second (conversion factor = 50/43)*/
    public static final float BULLET_SPEED = (50.0f / 43.0f) * 15.0f;
    
    private PooledEngine engine;
    
    public BulletSystem(PooledEngine e) {
        super(0);
        this.engine = e;
    }

    public BulletSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        BulletSpawnEvent.register(this);
    }   
    
    @Override
    public void removedFromEngine (Engine engine) {
        BulletSpawnEvent.unregister(this);
    }

    @Override
    public void onSpawnBullet(final float spawnX, final float spawnY, final float directionX, final float directionY,
                              final BiConsumer<Entity, Entity> onPlayerHit, 
                              final BiConsumer<Entity, Entity> onEntityHit, 
                              final Consumer<Entity> onHit) {
        
        //Create bullet entity
        Entity bulletEntity = engine.createEntity();
        
        //Create bullet component
        BulletComponent bulletComponent = engine.createComponent(BulletComponent.class);
        bulletComponent.onPlayerHit = onPlayerHit;
        bulletComponent.onEntityHit = onEntityHit;
        bulletComponent.onHit = onHit;
        
        //Create physics modifier component
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        
        //Bind components
        bulletEntity.add(bulletComponent);
        bulletEntity.add(modifyComponent);

        //Build bullet physics
        modifyComponent.schedule(() -> {
            PhysixSystem physixSystem = engine.getSystem(PhysixSystem.class);
            
            //Normalize direction
            float directionLen = (float) Math.sqrt(directionX * directionX + directionY * directionY);
            float nDirX = directionX / directionLen;
            float nDirY = directionY / directionLen;
            
            //Create physic Body component
            PhysixBodyComponent bulletBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem)
                                        .position(spawnX, spawnY)
                                        .fixedRotation(true);
            bulletBody.init(bodyDef, physixSystem, bulletEntity);
            
            //Bullet fixture
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                                          .density(1.0f)
                                          .friction(0.0f)
                                          .restitution(0.0f)
                                          .shapeCircle(5);
            bulletBody.createFixture(fixtureDef);
            
            //Force bullet
            bulletBody.applyImpulse(nDirX * BULLET_SPEED, nDirY * BULLET_SPEED);
            bulletBody.setGravityScale(0.0f);
            
            //Add body to entity
            bulletEntity.add(bulletBody);
        });
        engine.addEntity(bulletEntity);
        
    }
    
}
