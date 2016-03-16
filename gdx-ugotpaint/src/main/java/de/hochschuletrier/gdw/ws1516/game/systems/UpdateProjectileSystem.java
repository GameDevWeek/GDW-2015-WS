package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ProjectileComponent;

public class UpdateProjectileSystem extends IteratingSystem {

    private final Vector2 vel = new Vector2();

    public UpdateProjectileSystem() {
        this(0);
    }

    public UpdateProjectileSystem(int priority) {
        super(Family.all(ProjectileComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        ProjectileComponent projectile = ComponentMappers.projectile.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        vel.set(projectile.velocity).scl(deltaTime);
        position.pos.add(vel);
    }

}
