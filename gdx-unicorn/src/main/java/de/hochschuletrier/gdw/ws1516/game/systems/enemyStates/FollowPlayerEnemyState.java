package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;

public class FollowPlayerEnemyState extends EnemyBaseState {

    private static final Logger logger = LoggerFactory.getLogger(FollowPlayerEnemyState.class);

    // private LinkedList<Vector2> buffer=new LinkedList<>();
    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {

        EnemyBehaviourComponent behaviour = ComponentMappers.enemyBehaviour.get(entity);
        PositionComponent positionEnemy = ComponentMappers.position.get(entity);
        PositionComponent positionPlayer = ComponentMappers.position.get(player);
        // buffer.add(new Vector2(positionEnemy.x,positionEnemy.y));
        // if (buffer.size()>=GameConstants.ENEMY_FRAME_JUMP_BUFFER){
        // if(Vector2.dst(buffer.get(0).x, buffer.get(0).y, positionEnemy.x,
        // positionEnemy.y)<1f){
        // JumpEvent.emit(entity);
        // }
        // buffer.removeFirst();
        // }
        if (Math.abs(positionPlayer.x - positionEnemy.x) > GameConstants.TILESIZE_X/2) {
            if (positionPlayer.x < positionEnemy.x) {
                MovementEvent.emit(entity, -0.7f);
            } else {
                MovementEvent.emit(entity, 0.7f);
            }
        }else{
            MovementEvent.emit(entity, 0.0f);
        }

        if (behaviour.canSeeUnicorn) {
            if (behaviour.cooldown == 0) {
                MovementEvent.emit(entity, 0.0f);
                return new AttackEnemyState();
            } else {
                return this;
            }
        } else {
            return new FollowPathEnemyState();
        }
    }

}
