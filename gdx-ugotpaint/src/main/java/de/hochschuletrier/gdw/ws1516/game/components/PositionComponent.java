package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent extends Component implements Pool.Poolable {

    public final Vector2 pos = new Vector2();

    @Override
    public void reset() {
        pos.setZero();
    }
}
