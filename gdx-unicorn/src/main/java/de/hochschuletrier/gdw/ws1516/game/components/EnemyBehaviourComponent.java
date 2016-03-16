package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.DoNothingState;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.EnemyBaseState;

public class EnemyBehaviourComponent extends Component implements Poolable{

    public EnemyBaseState baseState;
    public boolean canSeeUnicorn;
    
    @Override
    public void reset() {
        baseState=null;
    }

}
