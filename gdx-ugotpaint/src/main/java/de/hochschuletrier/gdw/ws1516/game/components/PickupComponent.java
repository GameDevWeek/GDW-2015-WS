package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

public class PickupComponent extends Component implements Pool.Poolable {
    
    public Color color = Color.WHITE;

    @Override
    public void reset() {
        color = Color.WHITE;
    }

}
