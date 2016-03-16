package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.*;
import de.hochschuletrier.gdw.ws1516.events.*;

public class MovementSystem extends IteratingSystem implements
        StartFlyEvent.Listener, EndFlyEvent.Listener, JumpEvent.Listener,
        MovementEvent.Listener {
    private static final Logger logger = LoggerFactory
            .getLogger(MovementSystem.class);

    private PooledEngine e;

    // public MovementSystem(){
    // super(0);
    // }

    public MovementSystem(int priority) {
        super(Family.all(PhysixBodyComponent.class, MovementComponent.class).get(), priority);
        StartFlyEvent.register(this);
        EndFlyEvent.register(this);
        JumpEvent.register(this);
        MovementEvent.register(this);
    }

    // @Override
    // public void addedToEngine(Engine engine) {
    // super(engine);
    // logger.debug("Added to Engine{}");
    // // StartFlyEvent.register(this);
    // // EndFlyEvent.register(this);
    // };
    @Override
    public void removedFromEngine(com.badlogic.ashley.core.Engine engine) {
        // super(engine);
        StartFlyEvent.unregister(this);
        EndFlyEvent.unregister(this);
        JumpEvent.unregister(this);
        MovementEvent.unregister(this);
    };

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        // JumpComponent jump = ComponentMappers.jump.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);

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

        if (input != null) {
            MovementEvent.emit(entity, input.directionX);
            if (input.startJump
                    && movement.state == MovementComponent.State.ON_GROUND) {
                JumpEvent.emit(entity);
            }

        }
        physix.setLinearVelocityX(movement.velocityX);
    }

    private void moveWhileFlying(Entity entity, float delta) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);

        movement.velocityX = (movement.speed * input.directionX);
        movement.velocityY = (movement.speed * input.directionY);
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
            movement.state = MovementComponent.State.ON_GROUND;
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
        // TODO Auto-generated method stub
        MovementComponent movement = ComponentMappers.movement.get(entity);

        movement.velocityX = (movement.speed * dirX);

    }

}
