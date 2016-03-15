package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MovementStateComponent extends Component implements Pool.Poolable{

    public static enum State{
        ON_GROUND,
        FLYING,
        FALLING
    }
    public State state;
    
    @Override
    public void reset() {
        state = State.ON_GROUND;
    }

}
