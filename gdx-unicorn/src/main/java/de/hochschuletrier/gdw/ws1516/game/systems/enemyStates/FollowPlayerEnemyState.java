package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;

public class FollowPlayerEnemyState extends EnemyBaseState {

    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
    
        EnemyBehaviourComponent behaviour=ComponentMappers.enemyBehaviour.get(entity);
        PositionComponent positionEnemy=ComponentMappers.position.get(entity);
        PositionComponent positionPlayer=ComponentMappers.position.get(player);
        if ( positionPlayer.x < positionEnemy.x ){
            EnemyActionEvent.emit( entity,Type.MOVE ,-10.f);
        }else
        {
            EnemyActionEvent.emit( entity,Type.MOVE ,10.f);   
        }
        
        if ( behaviour.canSeeUnicorn )
        {
            return this;
        }else
        {  
            return new FollowPathEnemyState();
        }
    }

}
