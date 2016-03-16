package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.events.ShootEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ProjectileComponent;
import java.util.Iterator;

public class ShootSystem extends EntitySystem implements ShootEvent.Listener {

    private final Game game;

    public ShootSystem(Game game, int priority) {
        super(priority);
        this.game = game;
    }

    @Override
    public void addedToEngine(Engine engine) {
        ShootEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        ShootEvent.unregister(this);
    }

    @Override
    public void onShootEvent(Entity playerEntity) {
        PlayerComponent player = ComponentMappers.player.get(playerEntity);
        if(player.segments.size() > 2) {
            AnimationComponent animation = ComponentMappers.animation.get(playerEntity);
            InputComponent input = ComponentMappers.input.get(playerEntity);
            final PositionComponent pos = ComponentMappers.position.get(playerEntity);

            float x = pos.pos.x + input.lastMoveDirection.x * CollisionSystem.SEGMENT_DISTANCE;
            float y = pos.pos.y + input.lastMoveDirection.y * CollisionSystem.SEGMENT_DISTANCE;
            Entity entity = game.createEntity("projectile", x, y, animation.tint);
            ProjectileComponent projectile = ComponentMappers.projectile.get(entity);
            projectile.velocity.set(input.lastMoveDirection).nor().scl(120);
            
            pos.pos.set(player.removeFirstSegments(1));
        }
    }
}
