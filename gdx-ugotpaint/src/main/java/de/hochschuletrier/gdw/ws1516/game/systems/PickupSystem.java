package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Rectangle;
import de.hochschuletrier.gdw.ws1516.events.PickupEvent;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;
import java.util.Random;

public class PickupSystem extends EntitySystem implements PickupEvent.Listener {

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
    public void onPickupEvent(Entity player, Entity pickup) {
        createRandomPickup();
    }

    public void createRandomPickup() {
        Rectangle b = GameConstants.PICKUP_SPAWN_RECT;
        float x = b.x + (rand.nextFloat() * b.width - 1);
        float y = b.y + (rand.nextFloat() * b.height - 1);
        game.createEntity("pickup", x, y, PlayerColor.NEUTRAL);
    }
}
