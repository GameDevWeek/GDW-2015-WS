package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.events.EndgameEvent;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class UpdatePlayerSystem extends IteratingSystem implements GameOverEvent.Listener, EndgameEvent.Listener {

    private final Vector2 lastDir = new Vector2();
    private final Vector2 lastPoint = new Vector2();
    private final Vector2 dummy = new Vector2();
    protected boolean inputEnabled = true;
    private float endgameSpeedFactor = 1;

    public UpdatePlayerSystem() {
        this(0);
    }

    public UpdatePlayerSystem(int priority) {
        super(Family.all(PlayerComponent.class).get(), priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        GameOverEvent.register(this);
        EndgameEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        GameOverEvent.unregister(this);
        EndgameEvent.unregister(this);
    }
    
    @Override
    public void onGameOverEvent() {
        inputEnabled = false;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        if(inputEnabled) {
            InputComponent input = ComponentMappers.input.get(entity);
            dummy.set(input.moveDirection).nor().scl(80 * deltaTime * endgameSpeedFactor);
            position.pos.add(dummy);
            if(position.pos.x < GameConstants.BOUND_LEFT)
                position.pos.x = GameConstants.BOUND_LEFT;
            else if(position.pos.x > GameConstants.BOUND_RIGHT)
                position.pos.x = GameConstants.BOUND_RIGHT;
            if(position.pos.y < GameConstants.BOUND_TOP)
                position.pos.y = GameConstants.BOUND_TOP;
            else if(position.pos.y > GameConstants.BOUND_BOTTOM)
                position.pos.y = GameConstants.BOUND_BOTTOM;
        }
        PlayerComponent player = ComponentMappers.player.get(entity);

        updatePath(player, position);
        updateSegmentPositions(position, player);
    }

    private void updatePath(PlayerComponent player, PositionComponent position) {
        // add a path entry if last pos was set some distance ago
        float dist = player.path.getFirst().dst(position.pos);
        if (dist >= GameConstants.PATH_STEP_SIZE) {
            player.path.addFirst(position.pos.cpy());
            
            // reduce path size afterwards
            float minLength = Math.max(
                    player.segments.size() * GameConstants.SEGMENT_DISTANCE * 2,
                    GameConstants.SEGMENT_DISTANCE * 3);
            float totalDist = 0;
            Vector2 lastPoint = position.pos;
            int usedCount = 0;
            for (Vector2 path : player.path) {
                totalDist += path.dst(lastPoint);
                lastPoint = path;
                usedCount++;
                if (totalDist >= minLength) {
                    break;
                }
            }
            while (player.path.size() > usedCount) {
                player.path.removeLast();
            }
        }
    }

    private void updateSegmentPositions(PositionComponent position, PlayerComponent player) {
        // set segment positions based on path
        lastDir.setZero();
        dummy.setZero();
        lastPoint.set(position.pos);
        int pathIndex = 0;
        for (Vector2 seg : player.segments) {
            float toMove = GameConstants.SEGMENT_DISTANCE;
            while (toMove > 0) {
                if (pathIndex >= player.path.size()) {
                    dummy.set(lastDir).scl(toMove).add(lastPoint);
                    lastPoint.set(dummy);
                    break;
                }
                Vector2 destination = player.path.get(pathIndex);
                float possibleDistance = lastPoint.dst(destination);
                lastDir.set(destination).sub(lastPoint).nor();
                if (possibleDistance >= toMove) {
                    lastPoint.add(lastDir.x * toMove, lastDir.y * toMove);
                    if (possibleDistance == toMove) {
                        pathIndex++;
                    }
                    break;
                }
                toMove -= possibleDistance;
                lastPoint.set(destination);
                pathIndex++;
            }
            seg.set(lastPoint);
        }
    }

    @Override
    /**
     * Speeds up the players by given amount during the endgame.
     */
    public void onEndgameEvent() {
        endgameSpeedFactor = 2.5f;
    }
}
