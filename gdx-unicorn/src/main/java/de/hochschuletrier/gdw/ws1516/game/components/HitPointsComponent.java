package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HitPointsComponent extends Component implements Poolable {

    public int value;
    
    @Override
    public void reset() {
        value = 0;
    }

    public void decreaseHP() {
        value--;
    }
}
