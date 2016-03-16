package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class ProjectileComponent extends Component implements Pool.Poolable {

    public Vector2 velocity = new Vector2();
    
    @Override
    public void reset() {
        velocity.setZero();
    }
}
