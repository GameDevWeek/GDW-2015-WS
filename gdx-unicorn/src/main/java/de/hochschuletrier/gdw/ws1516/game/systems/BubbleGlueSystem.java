package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.BubbleGlueComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class BubbleGlueSystem extends EntitySystem {
    private static final Logger logger = LoggerFactory.getLogger(BubbleGlueSystem.class);
    
    private PooledEngine engine;
    private boolean groundTraceResult;
    
    public BubbleGlueSystem(PooledEngine engine) {
        this.engine = engine;
    }
    
    @Override
    public void update(float deltaTime) {
        
        ImmutableArray<Entity> entitys = engine.getEntitiesFor(Family.all(BubbleGlueComponent.class).get());
        for (Entity entity : entitys) {
              //Fetch components
                BubbleGlueComponent glueComponent = ComponentMapper.getFor(BubbleGlueComponent.class).get(entity);
                PhysixBodyComponent body = ComponentMappers.physixBody.get(glueComponent.gluedEntity);
                MovementComponent movement = ComponentMappers.movement.get(glueComponent.gluedEntity);
                
                //Remove glue if the glued body does no longer exist
                if (body == null) {
                    engine.removeEntity(entity);
                    continue;
                }
                                
                //Set entity to ground and arrest momentum
                checkIfEntityIsOnGround(glueComponent.gluedEntity, engine);
                if (!groundTraceResult) {
                    
                    //Glue (evtl. modify component)
                    body.setX(glueComponent.gluedToPosition.x);
                    body.setY(glueComponent.gluedToPosition.y);
                    
                    //Arresto momentum
                    body.setLinearVelocity(0, 0);
                    
                    //If the glued entity has a movement component, set it to glued
                    if (movement != null) 
                        movement.state = MovementComponent.State.GLUED;
                    
                } else {
                    
                    //High Gee-Force
                    body.setGravityScale(2.0f);
                    
                    //Set glue position
                    PhysixBodyComponent enemyBody = ComponentMappers.physixBody.get(glueComponent.gluedEntity);
                    glueComponent.gluedToPosition = enemyBody.getPosition();
                    
                }
                                
                //Cooldown
                glueComponent.timeRemaining -= deltaTime;
                    
                //Unglued entity
                if (glueComponent.timeRemaining <= 0) {
                    DeathEvent.emit(entity);
                    if (movement != null)
                        movement.state = MovementComponent.State.FALLING;
                }
                
        }
            
    }
    
    private void checkIfEntityIsOnGround(Entity e, Engine engine) {
        
        //Fetch position
        PhysixBodyComponent bodyComponent = e.getComponent(PhysixBodyComponent.class);
        if (bodyComponent != null) {

            //Fetch physix
            PhysixSystem physix = engine.getSystem(PhysixSystem.class);
            
            //Calculate ray vectors
            Vector2 entityEye = new Vector2(bodyComponent.getX(), bodyComponent.getY());
            Vector2 traceEnd  = new Vector2(entityEye.x, entityEye.y + 100);
            Vector2 rayStart = new Vector2();
            Vector2 rayEnd   = new Vector2();
            physix.toBox2D(entityEye, rayStart);
            physix.toBox2D(traceEnd, rayEnd);
            
            //reset trace result
            groundTraceResult = true;
            
            //raytrace
            physix.getWorld().rayCast((fixture, point, normal, fraction) -> {
                
                if (fixture.getBody().getType().equals(BodyType.StaticBody)) {
                    groundTraceResult = false;
                    return 0;
                }
                
                return 1;
            }, rayStart, rayEnd);
        }
        
    }
    
}