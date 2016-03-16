package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;

public class FollowPathEnemyState extends EnemyBaseState {

    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
        PathComponent pathComponent = ComponentMappers.path.get(entity);
        EnemyBehaviourComponent behaviour=ComponentMappers.enemyBehaviour.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        if (pathComponent!=null){
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
            if ( behaviour.lastJumped + 1.0f < timePassed )
            {
                if ( Math.abs(nextTarget.x - position.x) < position.y - nextTarget.y   )
                {
                    JumpEvent.emit(entity);
                    behaviour.lastJumped = timePassed;
                } else
                {
                    // zu lange stillgestanden
                }
            }
            if ( nextTarget.dst2(position.x,position.y) < 200 )
            {
                behaviour.pathIndex++;
                behaviour.pathIndex %= maxIndex;
            }
        }else{
            MovementEvent.emit(entity, 0.0f);
        }
        
        if ( behaviour.canSeeUnicorn )
        {
            return new FollowPlayerEnemyState();
        }else
        {
            return this;
        }
    }

}
