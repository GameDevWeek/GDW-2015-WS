package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class AnimationComponent extends Component implements Pool.Poolable 
{
    public String name;
    public HashMap<String, AnimationExtended> animationMap = new HashMap<>();
    public float stateTime;
    public boolean flipHorizontal;
    public boolean currentlyFlipped;
    public MovementComponent.State lastRenderedState;
    public AnimationExtended uninteruptableAnimation = null;
    public float xOffset;
    public float yOffset;
    public boolean lastRenderedIdle;    
    public float alpha;
    public boolean killWhenFinished;
    
    @Override
    public void reset() {
        killWhenFinished = false;
        animationMap = null;
        flipHorizontal = false;
        stateTime = 0;
    }
    
    public void resetStateTime()
    {
        stateTime = 0;
    }
}
