package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class AnimationComponent extends Component implements Pool.Poolable 
{
    public HashMap<String, AnimationExtended> animationMap = new HashMap<>();
    public float stateTime;
    public boolean flipHorizontal;

    
    @Override
    public void reset() {
        animationMap = null;
        flipHorizontal = false;
        stateTime = 0;
    }
}
