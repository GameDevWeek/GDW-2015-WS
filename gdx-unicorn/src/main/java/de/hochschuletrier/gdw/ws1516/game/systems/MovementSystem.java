package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.*;

public class MovementSystem extends IteratingSystem {
    private static final Logger logger = LoggerFactory
            .getLogger(MovementSystem.class);

    public MovementSystem(int priority) {
        super(Family.all(PhysixBodyComponent.class)
                .one(MovementComponent.class, PlayerComponent.class).get(),
                priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        // JumpComponent jump = ComponentMappers.jump.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);

        if (movement != null) {
            // normal move
            if (input != null) {
                movement.velocityX = (movement.speed * input.direction);
                physix.setLinearVelocityX(movement.velocityX);

                if (input.jump
                        && movement.state == MovementComponent.State.ON_GROUND) {
                    JumpEvent.emit(entity);
                    movement.state = MovementComponent.State.JUMPING;
                    physix.applyImpulse(0, movement.jumpImpulse);
                }
            }

        }

    }

}
