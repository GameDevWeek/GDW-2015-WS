package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;

public abstract class EnemyBaseState {
    protected float timePassed;
    public final EnemyBaseState compute(Entity entity,Entity player,float deltaTime){
        EnemyBehaviourComponent behaviour = ComponentMappers.enemyBehaviour.get(entity);
        behaviour.cooldown=Math.max(behaviour.cooldown-deltaTime, 0);
        timePassed+=deltaTime;
        return _compute(entity, player, deltaTime);
    }
    public abstract EnemyBaseState _compute(Entity entity,Entity player,float deltaTime);
}
