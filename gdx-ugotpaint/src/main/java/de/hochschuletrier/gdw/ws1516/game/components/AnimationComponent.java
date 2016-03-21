package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class AnimationComponent extends Component implements Pool.Poolable {

    public AnimationExtended animation;
    public float stateTime;
    public float rotation;
    public float alpha = 1;
    public float scale = 1;

    @Override
    public void reset() {
        animation = null;
        stateTime = 0;
        rotation = 0;
        alpha = 1;
    }
}
