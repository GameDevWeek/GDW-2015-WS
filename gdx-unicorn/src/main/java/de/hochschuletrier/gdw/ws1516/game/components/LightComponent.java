package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class LightComponent extends Component implements Pool.Poolable {
    public float intensity;

    @Override
    public void reset() {
        intensity = 0f;
    }    
}