package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent extends Component implements Pool.Poolable {

    public float x;
    public float y;
    public float oldX;
    public float oldY;
    public float rotation;

    @Override
    public void reset() {
        rotation = x = y = oldX = oldY = 0;
    }
}
