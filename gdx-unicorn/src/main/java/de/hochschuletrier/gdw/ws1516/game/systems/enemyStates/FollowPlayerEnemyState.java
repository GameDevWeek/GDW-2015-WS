package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;

public class FollowPlayerEnemyState extends EnemyBaseState {

    private LinkedList<Vector2> buffer=new LinkedList<>();
    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
    
        EnemyBehaviourComponent behaviour=ComponentMappers.enemyBehaviour.get(entity);
        PositionComponent positionEnemy=ComponentMappers.position.get(entity);
        PositionComponent positionPlayer=ComponentMappers.position.get(player);
        buffer.add(new Vector2(positionEnemy.x,positionEnemy.y));
        if (buffer.size()>=GameConstants.ENEMY_FRAME_JUMP_BUFFER){
            if(Vector2.dst(buffer.get(0).x, buffer.get(0).y, positionEnemy.x, positionEnemy.y)<1f){
                    EnemyActionEvent.emit(entity, Type.JUMP, -10000);
            }
            buffer.removeFirst();
        }
        if ( positionPlayer.x < positionEnemy.x ){
            EnemyActionEvent.emit( entity,Type.MOVE ,-45.f);
        }else
        {
            EnemyActionEvent.emit( entity,Type.MOVE ,45.f);   
        }
        
        if ( behaviour.canSeeUnicorn )
        {
            if (behaviour.canFireRange){
                EnemyActionEvent.emit( entity,Type.MOVE ,0.f); 
                return new AttackEnemyState();
            }else{
                return this;
            }
        }else
        {  
            return new FollowPathEnemyState();
        }
    }

}
