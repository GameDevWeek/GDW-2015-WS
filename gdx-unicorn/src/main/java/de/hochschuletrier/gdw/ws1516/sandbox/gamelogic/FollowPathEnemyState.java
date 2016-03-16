package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;

public class FollowPathEnemyState extends EnemyBaseState {

    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
        PathComponent pathComponent = ComponentMappers.path.get(entity);
        if (pathComponent!=null){
            //movetonextPathpoint;
        }
        EnemyBehaviourComponent behaviour=ComponentMappers.enemyBehaviour.get(entity);
        if (behaviour!=null && behaviour.canSeeUnicorn){
            return new FollowPlayerEnemyState();
        }
        return this;
    }

}
