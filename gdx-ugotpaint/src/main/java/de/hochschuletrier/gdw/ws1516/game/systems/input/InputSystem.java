package de.hochschuletrier.gdw.ws1516.game.systems.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;

public class InputSystem extends IteratingSystem {

    public InputSystem() {
        super(Family.all(InputComponent.class).get(), 0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent input = ComponentMappers.input.get(entity);
        PhysixBodyComponent physixBody = ComponentMappers.physixBody.get(entity);
        physixBody.setLinearVelocity(input.moveDirection.nor().scl(80));
//        physixBody.setAwake(true);
    }
}
