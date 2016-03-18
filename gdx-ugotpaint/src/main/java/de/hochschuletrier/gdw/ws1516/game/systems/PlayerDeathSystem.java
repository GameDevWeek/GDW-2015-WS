package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.events.PlayerDeathEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

import java.util.Random;

import static de.hochschuletrier.gdw.ws1516.game.GameConstants.*;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class PlayerDeathSystem extends EntitySystem implements PlayerDeathEvent.Listener {

    private Random rnd = new Random();

    private Entity playerRed, playerBlue;

    private ImmutableArray<Entity> players;

    public PlayerDeathSystem() {
        super();
    }

    public PlayerDeathSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        PlayerDeathEvent.register(this);

        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());

    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        PlayerDeathEvent.unregister(this);
    }

    @Override
    public void onPlayerDeathEvent(Entity player) {

        InputComponent input = ComponentMappers.input.get(player);
        Vector2 direction = input.lastMoveDirection;

        // on the first player death initialize the players
        if (playerRed == null) {
            playerRed = players.first();
            playerBlue = players.get(1);
        }

        PositionComponent position = ComponentMappers.position.get(player);
        PlayerComponent playerComponent = ComponentMappers.player.get(player);

        // trim path to the new size
        playerComponent.path.clear();

        Vector2 respawnLocation;
        PositionComponent otherPlayerPositionComponent;
        if (player == playerRed) {
            otherPlayerPositionComponent = ComponentMappers.position.get(playerBlue);
        } else {
            otherPlayerPositionComponent = ComponentMappers.position.get(playerRed);
        }
        // while the distance requirement is not met recalculate
        do {
            respawnLocation = calculateRespawn();
        } while (respawnLocation.dst(otherPlayerPositionComponent.pos) < GameConstants.MIN_RESPAWN_DISTANCE);

        position.pos.set(respawnLocation);

        // add more segments until the specified amount is reached
        for (int i = 1; i < GameConstants.DEFAULT_SEGMENTS; ++i) {
            playerComponent.segments.add(new Vector2());
        }
        // add the new paths oriented along the movement direction
        playerComponent.path.add(respawnLocation.add(direction.scl(-10)));
        for (int i = 1; i < GameConstants.DEFAULT_SEGMENTS; ++i) {
            playerComponent.path.add(playerComponent.path.get(i - 1).add(direction.scl(-10)));
        }
    }

    /**
     * calculates a new random respawn location
     * that needs to be checked with the minimum distance to the other player
     *
     * @return new respawn location as Vector2
     */
    private Vector2 calculateRespawn() {

        float x = rnd.nextInt((int) (BOUND_RIGHT - BORDER_SIZE - 2 * DEFAULT_SEGMENTS * SEGMENT_DISTANCE - 2 * DEFAULT_SEGMENTS * PAINT_RADIUS))
                + BORDER_SIZE + DEFAULT_SEGMENTS * SEGMENT_DISTANCE + DEFAULT_SEGMENTS * PAINT_RADIUS;


        float y = rnd.nextInt((int) (BOUND_BOTTOM - BORDER_SIZE - 2 * DEFAULT_SEGMENTS * SEGMENT_DISTANCE - 2 * DEFAULT_SEGMENTS * PAINT_RADIUS))
                + BORDER_SIZE + DEFAULT_SEGMENTS * SEGMENT_DISTANCE + DEFAULT_SEGMENTS * PAINT_RADIUS;

        return new Vector2(x, y);
    }
}
