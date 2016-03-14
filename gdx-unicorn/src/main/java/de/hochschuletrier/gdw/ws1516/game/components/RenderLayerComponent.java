package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class RenderLayerComponent extends Component implements Poolable{
    public int layer = 0;

    @Override
    public void reset ()
    {
        layer = 0;
    }
}