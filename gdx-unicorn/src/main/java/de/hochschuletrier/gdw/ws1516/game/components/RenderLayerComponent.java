package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class RenderLayerComponent extends Component implements Pool.Poolable{
    public int layer = 0;

    @Override
    public void reset ()
    {
        layer = 0;
    }
}