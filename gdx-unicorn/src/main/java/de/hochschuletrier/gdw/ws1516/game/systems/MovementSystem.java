package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.events.EndFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.StartFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.ThrowBackEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.LookDirection;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.factories.PhysixBodyComponentFactory;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;

public class MovementSystem extends IteratingSystem implements StartFlyEvent.Listener, EndFlyEvent.Listener, JumpEvent.Listener, MovementEvent.Listener, HornAttackEvent.Listener, ThrowBackEvent.Listener {
    private static final Logger logger = LoggerFactory.getLogger(MovementSystem.class);
    
    private PhysixSystem        physixSystem;
    
    // public MovementSystem(){
    // super(0);
    // }
    
    public MovementSystem(int priority) {
        super(Family.all(PhysixBodyComponent.class, MovementComponent.class).get(), priority);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        StartFlyEvent.register(this);
        EndFlyEvent.register(this);
        JumpEvent.register(this);
        MovementEvent.register(this);
        physixSystem = engine.getSystem(PhysixSystem.class);
        HornAttackEvent.register(this);
        ThrowBackEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(com.badlogic.ashley.core.Engine engine) {
        // super(engine);
        StartFlyEvent.unregister(this);
        EndFlyEvent.unregister(this);
        JumpEvent.unregister(this);
        MovementEvent.unregister(this);
        physixSystem = null;
        HornAttackEvent.unregister(this);
        ThrowBackEvent.unregister(this);
    };
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        // JumpComponent jump = ComponentMappers.jump.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        
        if (movement != null & input != null) {
            if (movement.lookDirection != input.lookDirection) { // richtung hat
                                                                 // sich
                                                                 // geaendert
                movement.lookDirection = input.lookDirection;
                PhysixBodyComponentFactory.recreatePlayerFixturesForDirection(physix, physixSystem, movement.lookDirection);
            }
        }
        
        if (movement != null) {
            switch (movement.state) {
                case FLYING:
                    moveWhileFlying(entity, deltaTime);
                    break;
                case FALLING:
                case JUMPING:
                case ON_GROUND:
                case LANDING:
                default:
                    defaultMovement(entity);
                    break;
            }
            
        }
        
    }
    
    private void defaultMovement(Entity entity) {
        // get Components
        
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        EnemyTypeComponent enemyType = ComponentMappers.enemyType.get(entity);
        
        if (input != null) {
            
            MovementEvent.emit(entity, input.directionX);
            if (input.startJump && (movement.state == MovementComponent.State.ON_GROUND || movement.state == MovementComponent.State.LANDING)) {
                JumpEvent.emit(entity);
            }
            
            boolean inAir = movement != null && (movement.state == MovementComponent.State.FLYING || movement.state == MovementComponent.State.FALLING || movement.state == MovementComponent.State.JUMPING);
            if ((inAir || input.directionX != 0 || input.directionY != 0) && physix != null) {
                physix.getFixtureByUserData("body").setFriction(0.0f);
            } else {
                physix.getFixtureByUserData("body").setFriction(1.0f);
            }
            
        }
        if (playerComp != null && playerComp.state != State.HORNATTACK && playerComp.state != State.THROWBACK) {
            if (movement.isOnPlatform) {
                physix.setLinearVelocityX(movement.velocityX + movement.onPlatformBody.getLinearVelocity().x);
            } else {
                physix.setLinearVelocityX(movement.velocityX);
            }
        }
        /**
         * Gegner wollen sich auch bewegen
         * 
         * @author Tobi - GameLogic
         */
        if (enemyType != null) {
            if (movement.isOnPlatform) {
                physix.setLinearVelocityX(movement.velocityX + movement.onPlatformBody.getLinearVelocity().x);
            } else {
                physix.setLinearVelocityX(movement.velocityX);
            }
        }
    }
    
    private void moveWhileFlying(Entity entity, float delta) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        PlayerComponent player = ComponentMappers.player.get(entity);
        
        movement.velocityX = movement.speed * Math.max(Math.min(input.directionX, 1.0f), -1.0f);
        movement.velocityY = movement.speed * Math.max(Math.min(input.directionY, 1.0f), -1.0f);
        
        physix.setLinearVelocity(movement.velocityX, movement.velocityY);
        // physix.applyImpulse(movement.velocityX, movement.velocityY);
        
        // timer:
        movement.remainingStateTime -= delta;
        if (movement.remainingStateTime <= 0) {
            EndFlyEvent.emit(entity);
        }
        
        if (player != null) {
            player.flyingCooldown = movement.remainingStateTime;
        }
    }
    
    @Override
    public void onEndFlyEvent(Entity entity) {
        // TODO Auto-generated method stub
        MovementComponent movement = ComponentMappers.movement.get(entity);
        if (movement != null) {
            MovementComponent.State newState;
            if (movement.contacts.isEmpty()) {
                newState = MovementComponent.State.FALLING;
            } else {
                newState = MovementComponent.State.ON_GROUND;
            }
            
            MovementStateChangeEvent.emit(entity, movement.state, newState);
            movement.state = newState;
        }
        
        PlayerComponent player = ComponentMappers.player.get(entity);
        if (player != null) {
            player.flyingCooldown = 0;
        }
    }
    
    @Override
    public void onStartFlyEvent(Entity entity, float time) {
        // TODO Auto-generated method stub
        MovementComponent movement = ComponentMappers.movement.get(entity);
        if (movement != null) {
            MovementComponent.State newState = MovementComponent.State.FLYING;
            MovementStateChangeEvent.emit(entity, movement.state, newState);
            movement.state = newState;
            movement.remainingStateTime = time;
        }
        
        PlayerComponent player = ComponentMappers.player.get(entity);
        if (player != null) {
            player.flyingCooldown = GameConstants.FLYING_TIME;
        }
    }
    
    @Override
    public void onJumpEvent(Entity entity) {
        // TODO Auto-generated method stub
        MovementComponent movement = ComponentMappers.movement.get(entity);
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        MovementComponent.State newState = MovementComponent.State.JUMPING;
        MovementStateChangeEvent.emit(entity, movement.state, newState);
        movement.state = newState;
        physix.applyImpulse(0, movement.jumpImpulse);
    }
    
    @Override
    public void onMovementEvent(Entity entity, float dirX) {
        MovementComponent movement = ComponentMappers.movement.get(entity);
        movement.velocityX = movement.speed * Math.max(Math.min(dirX, 1.0f), -1.0f);
        
        EnemyTypeComponent enemyType = ComponentMappers.enemyType.get(entity);
        if (enemyType != null) {
            if (dirX > 0.01) {
                movement.lookDirection = MovementComponent.LookDirection.RIGHT;
            } else if (dirX < -0.01) {
                movement.lookDirection = MovementComponent.LookDirection.LEFT;
            }
        }
        
    }
    
    @Override
    public void onHornAttackStart(Entity player) {
        // TODO Auto-generated method stub
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(player);
        InputComponent input = ComponentMappers.input.get(player);
        MovementComponent movement = ComponentMappers.movement.get(player);
        if (physix != null && input != null && movement != null) {
            physix.applyImpulse(movement.lookDirection.getCosine() * GameConstants.HORNATTACK_IMPULSE, 0);
            
            // movement.velocityX = movement.speed * 1000 * input.directionX;
        }
        
    }
    
    @Override
    public void onHornAttackStop(Entity player) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onThrowBackEventStart(Entity unicorn, float dirX) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(unicorn);
        MovementComponent movement = ComponentMappers.movement.get(unicorn);
        InputComponent input = ComponentMappers.input.get(unicorn);
        
        if (physix != null && movement != null && input != null) {
            MovementComponent.State newState = MovementComponent.State.FALLING;
            MovementStateChangeEvent.emit(unicorn, movement.state, newState);
            movement.state = newState;
            physix.setLinearVelocity(0, 0); // kills any movement
            
            //Compute throwback force
            float force = GameConstants.THROWBACK_FORCE;
            if (input.startJump) {
                force -= (float) Math.abs(movement.jumpImpulse);
            }
            
            physix.applyImpulse(dirX * force, -1.0f * force);
        }
    }
    
    @Override
    public void onThrowBackEventStop(Entity unicorn) {
    }
    
}
