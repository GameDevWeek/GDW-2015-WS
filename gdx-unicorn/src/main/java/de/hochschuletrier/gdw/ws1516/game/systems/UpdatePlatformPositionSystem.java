package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlatformComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class UpdatePlatformPositionSystem extends IteratingSystem {

    public UpdatePlatformPositionSystem() {
        this(0);
    }

    public UpdatePlatformPositionSystem(int priority) {
        super(Family.all(PlatformComponent.class, PathComponent.class, PhysixBodyComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PlatformComponent platform = ComponentMappers.platform.get(entity);
        platform.positionX = physix.getX();
        platform.positionY = physix.getY();
    }

}
