package de.hochschuletrier.gdw.ws1516.game.systems.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ws1516.events.ShootEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;

public class InputSystem extends IteratingSystem {

    public InputSystem() {
        super(Family.all(InputComponent.class).get(), 0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent input = ComponentMappers.input.get(entity);
        if(input.shoot) {
            ShootEvent.emit(entity);
            input.shoot = false;
        }
    }
}
