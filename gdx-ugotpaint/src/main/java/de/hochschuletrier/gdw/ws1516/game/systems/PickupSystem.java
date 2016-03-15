package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.PickupEvent;
import de.hochschuletrier.gdw.ws1516.game.Game;
import java.util.Random;

public class PickupSystem extends EntitySystem implements PickupEvent.Listener {

    private int scheduledPickups;
    private final Random rand = new Random();
    private final Game game;

    public PickupSystem(Game game) {
        this(game, 0);
    }

    public PickupSystem(Game game, int priority) {
        super(priority);
        this.game = game;
    }

    @Override
    public void addedToEngine(Engine engine) {
        PickupEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        PickupEvent.unregister(this);
    }

    @Override
    public void update(float deltaTime) {
        while (scheduledPickups > 0) {
            float x = rand.nextFloat() * Main.WINDOW_WIDTH - 1;
            float y = rand.nextFloat() * Main.WINDOW_HEIGHT - 1;
            game.createEntity("pickup", x, y, 0, 0, Color.WHITE);
            scheduledPickups--;
        }
    }

    @Override
    public void onPickupEvent(Entity player, Entity pickup) {
        schedulePickup();
    }

    public void schedulePickup() {
        scheduledPickups++;
    }
}
