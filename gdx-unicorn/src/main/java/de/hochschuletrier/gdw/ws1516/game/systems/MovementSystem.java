package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.EndFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.events.StartFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;

public class MovementSystem extends IteratingSystem implements StartFlyEvent.Listener, EndFlyEvent.Listener, JumpEvent.Listener, MovementEvent.Listener, HornAttackEvent.Listener {
    private static final Logger logger = LoggerFactory.getLogger(MovementSystem.class);
    
    private PooledEngine        e;
    
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
        HornAttackEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(com.badlogic.ashley.core.Engine engine) {
        // super(engine);
        StartFlyEvent.unregister(this);
        EndFlyEvent.unregister(this);
        JumpEvent.unregister(this);
        MovementEvent.unregister(this);
        HornAttackEvent.unregister(this);
    };
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        // JumpComponent jump = ComponentMappers.jump.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        
        if (movement != null & input != null)
            movement.lookDirection = input.lookDirection;
        
        if (movement != null) {
            switch (movement.state) {
                case FLYING:
                    moveWhileFlying(entity, deltaTime);
                    break;
                case FALLING:
                case JUMPING:
                case ON_GROUND:
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
        
        if (input != null) {
            MovementEvent.emit(entity, input.directionX);
            if (input.startJump && movement.state == MovementComponent.State.ON_GROUND) {
                JumpEvent.emit(entity);
            }
        }
        if (playerComp == null || playerComp.state != State.HORNATTACK) {
            physix.setLinearVelocityX(movement.velocityX);
        }
    }
    
    private void moveWhileFlying(Entity entity, float delta) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        
        movement.velocityX = movement.speed * Math.max(Math.min(input.directionX, 1.0f), -1.0f);
        movement.velocityY = movement.speed * Math.max(Math.min(input.directionY, 1.0f), -1.0f);
        
        physix.setLinearVelocity(movement.velocityX, movement.velocityY);
        // physix.applyImpulse(movement.velocityX, movement.velocityY);
        
        // timer:
        movement.remainingStateTime -= delta;
        if (movement.remainingStateTime <= 0) {
            EndFlyEvent.emit(entity);
        }
    }
    
    @Override
    public void onEndFlyEvent(Entity entity) {
        // TODO Auto-generated method stub
        MovementComponent movement = ComponentMappers.movement.get(entity);
        if (movement != null) {
            movement.state = MovementComponent.State.JUMPING;
        }
    }
    
    @Override
    public void onStartFlyEvent(Entity entity, float time) {
        // TODO Auto-generated method stub
        MovementComponent movement = ComponentMappers.movement.get(entity);
        if (movement != null) {
            movement.state = MovementComponent.State.FLYING;
            movement.remainingStateTime = time;
        }
    }
    
    @Override
    public void onJumpEvent(Entity entity) {
        // TODO Auto-generated method stub
        MovementComponent movement = ComponentMappers.movement.get(entity);
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        movement.state = MovementComponent.State.JUMPING;
        physix.applyImpulse(0, movement.jumpImpulse);
    }
    
    @Override
    public void onMovementEvent(Entity entity, float dirX) {
        
        MovementComponent movement = ComponentMappers.movement.get(entity);
        movement.velocityX = movement.speed * Math.max(Math.min(dirX, 1.0f), -1.0f);
        
    }
    
    @Override
    public void onHornAttackStart(Entity player) {
        // TODO Auto-generated method stub
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(player);
        InputComponent input = ComponentMappers.input.get(player);
        MovementComponent movement = ComponentMappers.movement.get(player);
        if (physix != null && input != null && movement != null) {
            physix.applyImpulse(input.directionX*GameConstants.HORNATTACK_IMPULSE, 0);
            
            // movement.velocityX = movement.speed * 1000 * input.directionX;
        }
        
    }
    
    @Override
    public void onHornAttackStop(Entity player) {
        // TODO Auto-generated method stub
        
    }
    
}
