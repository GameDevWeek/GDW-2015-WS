package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.utils.Timer;
import de.hochschuletrier.gdw.ws1516.events.EndgameEvent;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
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
public class PlayerDeathSystem extends EntitySystem implements PlayerDeathEvent.Listener ,EndgameEvent.Listener {

    private Random rnd = new Random();

    private Game game;

    private Vector2 direction;

    private Entity playerRed, playerBlue;

    private Entity currentlyDeadPlayer;

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
        this.engine = engine;
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());

    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        PlayerDeathEvent.unregister(this);
    }

    @Override
    public void onPlayerDeathEvent(Entity player) {

        timer = new Timer();
        timer.reset();

        // on the first player death initialize the players
        if (playerRed == null) {
            playerRed = players.first();
            playerBlue = players.get(1);
        }

        InputComponent input = ComponentMappers.input.get(player);
        direction = input.lastMoveDirection;
        PlayerComponent playerComponent = ComponentMappers.player.get(player);
        playerComponent.segments.clear();
        deadPlayers.add(playerComponent.color);
        engine.removeEntity(player);


    }

    private void respawnPlayer(PlayerColor player) {

        Vector2 respawnLocation;
        PositionComponent otherPlayerPositionComponent;

        int playerIndex;

        if (player == PlayerColor.RED) {
            otherPlayerPositionComponent = ComponentMappers.position.get(playerBlue);
            playerIndex = 0;
        } else {
            otherPlayerPositionComponent = ComponentMappers.position.get(playerRed);
            playerIndex = 1;
        }
        // while the distance requirement is not met recalculate
        do {
            respawnLocation = calculateRespawn();
        }
        while (otherPlayerPositionComponent != null && respawnLocation.dst(otherPlayerPositionComponent.pos) < GameConstants.MIN_RESPAWN_DISTANCE);

        Entity newPlayer = game.createSnake(playerIndex, respawnLocation.x, respawnLocation.y, direction.x, direction.y, player);
        PlayerComponent playerComponent = ComponentMappers.player.get(newPlayer);

//        position.pos.set(respawnLocation);

        if (playerComponent != null) {

            // add more segments until the specified amount is reached
            for (int i = 1; i < GameConstants.DEFAULT_SEGMENTS; ++i) {
                playerComponent.segments.add(new Vector2());
            }
            // add the new paths oriented along the movement direction
            playerComponent.path.add(respawnLocation.add(direction.scl(-10)));
        }
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
    public void update(float deltaTime) {
        super.update(deltaTime);

        // if respawn delay has passed respawn the player
        if (timer != null) {
            if (timer.get() >= GameConstants.RESPAWN_DELAY) {
                for (PlayerColor pc : deadPlayers) {
                    respawnPlayer(pc);
                }
                deadPlayers.clear();
            }
        }
    }

    @Override
    public void onEndgameEvent() {
        endGame = true;
    }
}
