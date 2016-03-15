package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.*;

public class MovementSystem extends IteratingSystem {

    public MovementSystem(int priority) {
        super(Family
                .all(PhysixBodyComponent.class)
                .one(MovementComponent.class, JumpComponent.class,
                        PlayerComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        JumpComponent jump = ComponentMappers.jump.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);


        if (movement != null) {
            //normal move
            if (input != null) {
                movement.velocityX = (movement.speed * input.direction);
            }

            // jumps
            if (jump != null) {
                // if jump was called
                if (input != null) {
                    if (input.jump && jump.isGrounded) {
                        JumpEvent.emit(entity);
                        jump.isGrounded = false;
                        physix.applyImpulse(0, jump.jumpImpulse);
                    }
                }

            }

        }
    }

}
