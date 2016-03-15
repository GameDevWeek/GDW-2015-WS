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
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class ShootSystem extends EntitySystem implements ShootEvent.Listener {

    private final Game game;
    private final Vector2 vector = new Vector2();

    public ShootSystem(Game game) {
        this(game, 0);
    }

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
    public void onShootEvent(Entity player) {
        AnimationComponent animation = ComponentMappers.animation.get(player);
        InputComponent input = ComponentMappers.input.get(player);
        PositionComponent position = ComponentMappers.position.get(player);
        vector.set(input.lastMoveDirection);
        vector.nor().scl(120);
        float x = position.x;
        float y = position.y;
        game.createEntity("projectile", x, y, vector.x, vector.y, animation.tint);
    }
}
