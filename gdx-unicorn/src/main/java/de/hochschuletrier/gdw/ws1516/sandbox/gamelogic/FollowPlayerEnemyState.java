package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import com.badlogic.ashley.core.Entity;

public class FollowPlayerEnemyState extends EnemyBaseState {

    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
        /*
         * if (entity in range & entity has no cooldown){
         *      return new AttackEnemyState();
         * }else{
         *      EnemyActionEvent.emit(entity,Type.Move,XYZ);
         *      return this;
         * }
         * 
         */
        return new DoNothingState();
    }

}
