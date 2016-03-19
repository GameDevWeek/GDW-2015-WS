package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.events.BlockingGumSpawnEvent;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.BlockingGumComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;

/**
 * This system handel's blocking gum.
 * Blocking gum is spawned by a gum spit that hit the ground
 * @author Eileen
 * @version 1.0
 */
public class BlockingGumSystem extends IteratingSystem implements BlockingGumSpawnEvent.Listener,
                                                                  RainbowEvent.Listener {
    private static final Logger logger = LoggerFactory.getLogger(BlockingGumSystem.class);
    
    private PooledEngine engine;
    private boolean isRainbow;
    
        
    @SuppressWarnings("unchecked")
    public BlockingGumSystem(PooledEngine engine) {
        super(Family.all(BlockingGumComponent.class,
                         PositionComponent.class,
                         PhysixBodyComponent.class).get());
        this.engine = engine;
        this.isRainbow = false;
    }
    
    @Override
    public void addedToEngine (Engine engine) {
        super.addedToEngine(engine);
        BlockingGumSpawnEvent.register(this);
    }

    @Override
    public void removedFromEngine (Engine engine) {
        super.removedFromEngine(engine);
        BlockingGumSpawnEvent.unregister(this);
    }
    
    @Override
    public void onSpawnBlockingGum(float spawnX, float spawnY, float angle, Consumer<Entity> onDespawn) {
        
        //Create callback
        Entity blockingGumEntity;
        if (isRainbow) {
            blockingGumEntity = EntityCreator.createEntity("blockingGumRainbow", spawnX, spawnY);
        } else {
            blockingGumEntity = EntityCreator.createEntity("blockingGum", spawnX, spawnY);
        }
            
        //Set callback's
        BlockingGumComponent blockingComponent = ComponentMappers.blockinggum.get(blockingGumEntity);
        blockingComponent.onDespawn = onDespawn;
        
        //Add body manually
        blockingGumEntity.add(engine.createComponent(PhysixBodyComponent.class));
        PhysixModifierComponent phyModifier = engine.createComponent(PhysixModifierComponent.class);
        phyModifier.schedule(() -> {
            
            //Get physix system
            PhysixSystem physix = engine.getSystem(PhysixSystem.class);

            //Create physic Body component
            PhysixBodyComponent blockingBody = ComponentMappers.physixBody.get(blockingGumEntity);
            PhysixBodyDef blockingBodyDef = new PhysixBodyDef(BodyType.StaticBody, physix)
                                            .position(spawnX, spawnY)
                                            .fixedRotation(false);
            blockingBody.init(blockingBodyDef, physix, blockingGumEntity);
            
            //Gum spit fixture
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physix)
                                          .density(1.0f)
                                          .friction(0.0f)
                                          .restitution(0.0f)
                                          .shapeBox(64.0f, 30.0f);
            fixtureDef.filter.groupIndex = GameConstants.PHYSIX_COLLISION_BLOCKINGGUM;
            blockingBody.createFixture(fixtureDef);
            
            //Rotate to match normal
            TextureComponent texture = ComponentMappers.texture.get(blockingGumEntity);
            PositionComponent position = ComponentMappers.position.get(blockingGumEntity);
            if (position != null) {
                texture.originX = GameConstants.SPIT_BLOCKING_ORIGINX;
                texture.originY = GameConstants.SPIT_BLOCKING_ORIGINY;
                blockingBody.setAngle(angle * PhysixUtil.DEG2RAD);
            }
            
            //Add body to entity
            blockingGumEntity.add(blockingBody);
            
        });
        blockingGumEntity.add(phyModifier);
        
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BlockingGumComponent blockingComponent = ComponentMappers.blockinggum.get(entity);
        
        if (blockingComponent != null) {
            
            //The clock ticks life away
            //It's so unreal...
            //Didn't look out below
            //Watch the time go right out the window
            //Trying to hold on but didn't even know
            //I wasted it all just to
            //Watch you go...
            blockingComponent.timeToDespawn -= deltaTime;
            
            //Alpha depending on clock
            if (blockingComponent.timeToDespawn <= GameConstants.SPIT_BLOCKING_ALPHA_START) {
                TextureComponent texture = ComponentMappers.texture.get(entity);
                if (texture != null && GameConstants.SPIT_BLOCKING_ALPHA_START > 0.0f) {
                    texture.alpha = 1.0f - (blockingComponent.timeToDespawn / GameConstants.SPIT_BLOCKING_ALPHA_START);
                }
            }
            
            //Despawn on clock <= 0
            if (blockingComponent.timeToDespawn <= 0) {
                blockingComponent.onDespawn.accept(entity);
                engine.removeEntity(entity);
            }
                
        }
        
        
    }

    @Override
    public void onRainbowCollect(Entity player) {
        this.isRainbow = true;
    }

    @Override
    public void onRainbowModeEnd(Entity player) {
        this.isRainbow = false;
    }
    
}