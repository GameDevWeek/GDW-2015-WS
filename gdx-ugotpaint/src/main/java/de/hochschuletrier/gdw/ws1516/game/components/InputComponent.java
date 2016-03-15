package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class InputComponent extends Component implements Pool.Poolable {

    public final Vector2 moveDirection = new Vector2();
    public final Vector2 lastMoveDirection = new Vector2();
    public boolean shoot;
    public int index;

    @Override
    public void reset() {
        moveDirection.setZero();
        lastMoveDirection.set(0, 1);
        shoot = false;
    }
    
    public void setFrom(InputComponent other) {
        moveDirection.set(other.moveDirection);
        lastMoveDirection.set(other.lastMoveDirection);
        shoot = other.shoot;
        index = other.index;
    }
}
