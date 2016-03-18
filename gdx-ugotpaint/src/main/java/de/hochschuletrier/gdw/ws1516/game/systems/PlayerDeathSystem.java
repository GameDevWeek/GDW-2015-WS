package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.events.PlayerDeathEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class PlayerDeathSystem extends EntitySystem implements PlayerDeathEvent.Listener {

    private RandomXS128 rnd = new RandomXS128();

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
        Vector2 direction = input.moveDirection;

        // on the first player death initialize the players
        if (playerRed == null) {
            playerRed = players.first();
            playerBlue = players.get(1);
        }

        PositionComponent position = ComponentMappers.position.get(player);
        PlayerComponent playerComponent = ComponentMappers.player.get(player);
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
        for(int i = 0; i < playerComponent.segments.size(); ++i){
            playerComponent.path.add(new Vector2(playerComponent.segments.get(i).add(new Vector2(direction).scl(10))));
        }
    }

    /**
     * calculates a new random respawn location
     * that needs to be checked with the minimum distance to the other player
     *
     * @return new respawn location as Vector2
     */
    private Vector2 calculateRespawn() {
        float x = rnd.nextLong((long) (GameConstants.BOUND_WIDTH - GameConstants.BORDER_SIZE))
                + GameConstants.DEFAULT_SEGMENTS * GameConstants.PAINT_RADIUS + GameConstants.BORDER_SIZE;
        float y = rnd.nextLong((long) (GameConstants.BOUND_HEIGHT - GameConstants.BORDER_SIZE))
                + GameConstants.DEFAULT_SEGMENTS * GameConstants.PAINT_RADIUS + GameConstants.BORDER_SIZE;

        return new Vector2(x, y);
    }
}
