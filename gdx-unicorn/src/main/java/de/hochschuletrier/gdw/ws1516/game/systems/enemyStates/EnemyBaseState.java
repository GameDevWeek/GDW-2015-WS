package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import com.badlogic.ashley.core.Entity;

public abstract class EnemyBaseState {
    protected float timePassed;
    public EnemyBaseState compute(Entity entity,Entity player,float deltaTime){
        timePassed+=deltaTime;
        return _compute(entity, player, deltaTime);
    }
    public abstract EnemyBaseState _compute(Entity entity,Entity player,float deltaTime);
}
