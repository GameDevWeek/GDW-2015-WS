package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class InputComponent extends Component implements Pool.Poolable {

    public final Vector2 moveDirection = new Vector2();
    public boolean shoot;
    public int index;

    @Override
    public void reset() {
        moveDirection.setZero();
        shoot = false;
    }
}
