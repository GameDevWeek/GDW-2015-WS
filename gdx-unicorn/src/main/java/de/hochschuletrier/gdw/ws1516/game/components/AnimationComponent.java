package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class AnimationComponent extends Component implements Pool.Poolable 
{
    public HashMap<String, AnimationExtended> animationMap = new HashMap<>();
    public AnimationState animationState = AnimationState.none;
    public float stateTime;
    public int layer;
    public boolean flipHorizontal;
    
    public enum AnimationState 
    {
        none,
        bulletSpawn,
        death,
        heal,
        hit,
        jump
    }

    @Override
    public void reset() {
        animationMap = new HashMap<>();
        stateTime = 0;
        layer = 0;
    }
}
