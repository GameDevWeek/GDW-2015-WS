package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class UpdatePlayerSystem extends IteratingSystem {

    private final Vector2 vel = new Vector2();

    public UpdatePlayerSystem() {
        this(0);
    }

    public UpdatePlayerSystem(int priority) {
        super(Family.all(PlayerComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        InputComponent input = ComponentMappers.input.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        PlayerComponent player = ComponentMappers.player.get(entity);
        vel.set(input.moveDirection).nor().scl(80 * deltaTime);
        position.pos.add(vel);

        updatePath(player, position);
        updateSegmentPositions(position, player);
    }

    private void updatePath(PlayerComponent player, PositionComponent position) {
        // add a path entry if last pos was set some distance ago
        float dist = player.path.getFirst().dst(position.pos);
        if (dist >= ComponentMappers.PATH_STEP_SIZE) {
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
        final Vector2 lastDir = new Vector2();
        final Vector2 lastPoint = position.pos.cpy();
        int pathIndex = 0;
        for (Vector2 seg : player.segments) {
            float toMove = GameConstants.SEGMENT_DISTANCE;
            while (toMove > 0) {
                if (pathIndex >= player.path.size()) {
                    lastPoint.set(lastDir).scl(toMove).add(lastPoint);
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

}
