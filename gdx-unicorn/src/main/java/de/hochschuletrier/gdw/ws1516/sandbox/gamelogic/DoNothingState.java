package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import com.badlogic.ashley.core.Entity;

public class DoNothingState extends EnemyBaseState {

    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
        return this;
    }

}
