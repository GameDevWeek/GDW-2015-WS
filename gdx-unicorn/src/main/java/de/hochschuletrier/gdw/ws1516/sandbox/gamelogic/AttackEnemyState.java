package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;

public class AttackEnemyState extends EnemyBaseState {

    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
        EnemyActionEvent.emit(entity, Type.SHOOT, 10f);
        return new FollowPlayerEnemyState();
    }

}
