package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class AnimationComponent extends Component implements Pool.Poolable {

    public AnimationExtended animation;
    public float stateTime;
    public int layer;
    public Color tint = Color.WHITE;

    @Override
    public void reset() {
        animation = null;
        stateTime = 0;
        layer = 0;
        tint = Color.WHITE;
    }
}
