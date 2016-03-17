package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent.EnemyType;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;

public class FollowPathEnemyState extends EnemyBaseState {

    private static final Logger logger = LoggerFactory.getLogger(FollowPathEnemyState.class);
    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
        PathComponent pathComponent = ComponentMappers.path.get(entity);
        EnemyBehaviourComponent behaviour=ComponentMappers.enemyBehaviour.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        EnemyTypeComponent enemyType=ComponentMappers.enemyType.get(entity);
        if ( behaviour.canSeeUnicorn )
        {
            MovementEvent.emit(entity, 0.0f);
            if (enemyType.type==EnemyType.HUNTER){
                if (behaviour.cooldown==0){
                    return new AttackEnemyState();
                }
                else{
                    return this;
                }
            }
            return new FollowPlayerEnemyState();
        }
        if (pathComponent!=null && pathComponent.points.size()>0){
            /// index für den Punkt raszufinden der von 0 - max -  0 pendelt
            int pathIndex;
            int maxIndex = pathComponent.points.size()*2 -2;
            if (behaviour.pathIndex <pathComponent.points.size())
            {
                pathIndex = behaviour.pathIndex;
            }else
            {
                pathIndex = pathComponent.points.size()-2-behaviour.pathIndex%pathComponent.points.size();
            }
            
            
            Vector2 nextTarget = pathComponent.points.get(pathIndex);
            
            if ( nextTarget.x < position.x )
            {
                MovementEvent.emit(entity, -1.0f);
            }else
            {
                MovementEvent.emit(entity, 1.0f);
            }
            //if ( nextTarget.dst2(position.x,position.y) < 100 )
            if (Math.abs(nextTarget.x-position.x)<10)
            {
                behaviour.pathIndex++;
                behaviour.pathIndex %= maxIndex;
            }
        }else{
            MovementEvent.emit(entity, 0.0f);
        }
        return this;
    }

}
