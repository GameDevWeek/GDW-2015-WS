package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.utils.Timer;
import de.hochschuletrier.gdw.ws1516.events.EndgameEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerDeathEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

import java.util.LinkedList;
import java.util.Random;

import static de.hochschuletrier.gdw.ws1516.game.GameConstants.*;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class PlayerDeathSystem extends EntitySystem implements PlayerDeathEvent.Listener, EndgameEvent.Listener {

    private Random rnd = new Random();

    private Game game;

    private Vector2 direction = new Vector2();

    private Entity playerRed, playerBlue;

    private LinkedList<PlayerColor> deadPlayers = new LinkedList<>();

    private Timer timer;

    private boolean endGame;

    private Engine engine;

    private ImmutableArray<Entity> players;
    private final Vector2 dummy = new Vector2();

    public PlayerDeathSystem(Game game) {
        super();
        this.game = game;
    }

    public PlayerDeathSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        PlayerDeathEvent.register(this);
        EndgameEvent.register(this);
        this.engine = engine;
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());

    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        PlayerDeathEvent.unregister(this);
        EndgameEvent.unregister(this);
    }


    @Override
    /** Kills the player, removes it from the engine
     * and adds it to the dead players list to schedule them for respawn
     */
    public void onPlayerDeathEvent(Entity player) {
        timer = new Timer();
        timer.reset();

        // on the first player death initialize the players
        if (ComponentMappers.player.get(players.first()).color == PlayerColor.RED) {
            playerRed = players.first();
            if (players.size() > 1) {
                playerBlue = players.get(1);
            }
        } else {
            playerBlue = players.first();
            if (players.size() > 1) {
                playerRed = players.get(1);
            }
        }


        InputComponent input = ComponentMappers.input.get(player);
        direction.set(input.lastMoveDirection);
        PlayerComponent playerComponent = ComponentMappers.player.get(player);
        playerComponent.segments.clear();
        playerComponent.path.clear();
        deadPlayers.add(playerComponent.color);
        engine.removeEntity(player);

    }

    /**
     * Creates a new player entity according to the dead one and spawns it into the level.
     * Currently the path is not set correctly due to a problem with the entity creation.
     *
     * @param player the player that is to be respawned.
     */
    private void respawnPlayer(PlayerColor player) {

        Vector2 respawnLocation;
        PositionComponent otherPlayerPositionComponent;
        LinkedList<Vector2> segments = new LinkedList<>();
        int playerIndex;


        if (player == PlayerColor.RED) {
            otherPlayerPositionComponent = ComponentMappers.position.get(playerBlue);
            playerIndex = 0;
            if (ComponentMappers.player.get(playerBlue) != null) {
                segments = ComponentMappers.player.get(playerBlue).segments;
            }
        } else {
            otherPlayerPositionComponent = ComponentMappers.position.get(playerRed);
            playerIndex = 1;
            if (ComponentMappers.player.get(playerRed) != null) {
                segments = ComponentMappers.player.get(playerRed).segments;
            }
        }
        // while the distance requirement is not met recalculate
        int tries = 0;
        boolean distanceIsOk = true;
        do {
            respawnLocation = calculateRespawn();
            // checks the distance to every single enemy segment
            if (!segments.isEmpty()) {
                for (Vector2 segment : segments) {
                    if (respawnLocation.dst(segment) < GameConstants.MIN_RESPAWN_DISTANCE) {
                        distanceIsOk = false;
                    }
                }
            }
            ++tries;
        }
        // fuck it after 10 tries!
        while (!distanceIsOk && tries < 10);

        Entity newPlayer = game.createSnake(playerIndex, respawnLocation.x, respawnLocation.y, direction.x, direction.y, player);
        PlayerComponent playerComponent = ComponentMappers.player.get(newPlayer);

        // add more segments until the specified amount is reached
        while (playerComponent.segments.size() < GameConstants.DEFAULT_SEGMENTS - 1) {
            playerComponent.segments.add(new Vector2());
        }
        // add the new paths oriented along the movement direction
        playerComponent.path.add(respawnLocation.add(direction.scl(-10)));
        timer = null;
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

        return dummy.set(x, y);
    }

    @Override
    /** Calls respawnPlayer for every dead player after the timer has run out
     *  or calls it immediately if the game is in the endgame state.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (!deadPlayers.isEmpty()) {
            if (endGame || timer != null) {
                if (endGame || timer.get() >= GameConstants.RESPAWN_DELAY) {
                    deadPlayers.forEach(this::respawnPlayer);
                    deadPlayers.clear();
                }
            }
        }
    }

    @Override
    /** Sets the endGame bool to manage faster respawn */
    public void onEndgameEvent() {
        endGame = true;
    }
}
