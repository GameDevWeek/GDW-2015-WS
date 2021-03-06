package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
import de.hochschuletrier.gdw.ws1516.events.BlockingGumSpawnEvent;
import de.hochschuletrier.gdw.ws1516.events.BubblegumSpitSpawnEvent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.BubblegumGlueSpawnEvent;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.BubbleGlueComponent;
import de.hochschuletrier.gdw.ws1516.game.components.BubblegumSpitComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.BubblegumSpitListener.CollisionDetails;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;

/**
 * Handles bubble-gum spits
 * @author Eileen
 * @version 1.0
 */
public class BubblegumSpitSystem extends IteratingSystem implements BubblegumSpitSpawnEvent.Listener,
                                                                    BubblegumGlueSpawnEvent.Listener,
                                                                    RainbowEvent.Listener {
    private static final Logger logger = LoggerFactory.getLogger(BubblegumSpitSystem.class);
    
    private PooledEngine engine;
    private boolean spitsRainbow;
    
    @SuppressWarnings("unchecked")
    public BubblegumSpitSystem(PooledEngine engine) {
        super(Family.all(BubblegumSpitComponent.class,
                         PositionComponent.class,
                         PhysixBodyComponent.class).get());
        this.engine = engine;
        this.spitsRainbow = false;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        BubblegumSpitSpawnEvent.register(this);
        BubblegumGlueSpawnEvent.register(this);
        RainbowEvent.register(this);
    }   
    
    @Override
    public void removedFromEngine (Engine engine) {
        super.removedFromEngine(engine);
        BubblegumSpitSpawnEvent.unregister(this);
        BubblegumGlueSpawnEvent.unregister(this);
        RainbowEvent.unregister(this);
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
    public void onSpawnBubblegumSpit(final float force) {
        
        //Resolve player entity
        @SuppressWarnings("unchecked")
        Entity player = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                                                         MovementComponent.class,
                                                         PositionComponent.class).get()).first();
        PositionComponent playerPos  = ComponentMappers.position.get(player);
        MovementComponent playerMove = ComponentMappers.movement.get(player); 
        
        //Calculate spawn position
        float playerLookCosine = playerMove.lookDirection.getCosine();
        float spawnX = playerPos.x + GameConstants.SPIT_SPAWN_OFFSET_X * playerLookCosine;
        float spawnY = playerPos.y + GameConstants.SPIT_SPAWN_OFFSET_Y;
        
        //Create gum spit entity, rainbow or not
        Entity gumSpitEntity;
        if (spitsRainbow) {
            gumSpitEntity = EntityCreator.createEntity("bubblegum_spitting_rainbow", spawnX, spawnY);
        } else {
            gumSpitEntity = EntityCreator.createEntity("bubblegum_spitting", spawnX, spawnY);
        }
            
        //Set callback's
        BubblegumSpitComponent spitComponent = ComponentMappers.bubblegumSpitComponent.get(gumSpitEntity);
        spitComponent.onEnemyHit = (gum, enemy) -> BubblegumGlueSpawnEvent.emit(gum, enemy);
        spitComponent.onHit = this::removeBubblegumSpit;
        
        //Calculate spit impulse
        float spitForceDelta = GameConstants.SPIT_FORCE_MAX - GameConstants.SPIT_FORCE_MIN;
        float impulseForce = GameConstants.SPIT_FORCE_MIN + force * spitForceDelta;
        float impulseX = (float) Math.cos(GameConstants.SPIT_SPAWN_ANGLE) * playerLookCosine;
        float impulseY = (float) Math.sin(GameConstants.SPIT_SPAWN_ANGLE);
        
        //Add body manually
        gumSpitEntity.add(engine.createComponent(PhysixBodyComponent.class));
        PhysixModifierComponent phyModifier = engine.createComponent(PhysixModifierComponent.class);
        phyModifier.schedule(() -> {
            
            //Get physix system
            PhysixSystem physix = engine.getSystem(PhysixSystem.class);
            
            //Create physic Body component
            PhysixBodyComponent spitBodyComponent = ComponentMappers.physixBody.get(gumSpitEntity);
            PhysixBodyDef spitBodyDef = new PhysixBodyDef(BodyType.DynamicBody, physix)
                                            .position(spawnX, spawnY)
                                            .fixedRotation(true);
            spitBodyComponent.init(spitBodyDef, physix, gumSpitEntity);
            
            //Gum spit fixture
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physix)
                                          .density(1.0f)
                                          .friction(0.0f)
                                          .restitution(0.0f)
                                          .shapeCircle(5);
            fixtureDef.filter.groupIndex = GameConstants.PHYSIX_COLLISION_SPIT;
            spitBodyComponent.createFixture(fixtureDef);
            
            //Set sprite origin
            TextureComponent texture = ComponentMappers.texture.get(gumSpitEntity);
            texture.originX = 16.0f;
            texture.originY =  8.0f;
            
            //Force gum spit
            spitBodyComponent.applyImpulse(impulseX * impulseForce, impulseY * impulseForce);
            spitBodyComponent.setGravityScale(1.0f);
            
            //Add body to entity
            gumSpitEntity.add(spitBodyComponent);
           
        });
        gumSpitEntity.add(phyModifier);
        
    }
    
    @Override
    public void onSpawnGlue(Entity gum, Entity gluedEntity) {
        if (ComponentMappers.physixBody.get(gluedEntity) == null)
            return;
        
        //Fetch body
        PhysixBodyComponent enemyBody = ComponentMapper.getFor(PhysixBodyComponent.class).get(gluedEntity);
                    
        //Create glue entity
        final Entity glueEntity = engine.createEntity();
     
        //Set glue component
        BubbleGlueComponent glueComponent = engine.createComponent(BubbleGlueComponent.class);
        glueComponent.gluedEntity = gluedEntity;
        glueComponent.gluedToPosition = new Vector2(enemyBody.getX(), enemyBody.getY());
        glueComponent.timeRemaining = GameConstants.SPIT_GLUE_COOLDOWN;
            
        glueEntity.add(glueComponent);
            
        engine.addEntity(glueEntity);
        DeathEvent.emit(gum);
    }
    
    @Override
    public void onRainbowCollect(Entity player) {
        this.spitsRainbow = true;
    }

    @Override
    public void onRainbowModeEnd(Entity player) {
        this.spitsRainbow = false;
    }
    
    private void removeBubblegumSpit(Entity gum, CollisionDetails details) {
        
        //Spawn blocking gum when no enemy was hit
        if (!details.hitEnemy)
            spawnBlockingGum(gum, details.normal);
        
        //Remove the spit
        engine.removeEntity(gum);
        
    }

    private void spawnBlockingGum(Entity gum, Vector2 normal) {
        PositionComponent position = ComponentMappers.position.get(gum);
        if (position != null) { 
            

            BlockingGumSpawnEvent.emit(position.x, position.y, normal.angle() + 270.0f, (blockingGum) -> {
                engine.removeEntity(blockingGum);
            });
        }
    }
    
}
