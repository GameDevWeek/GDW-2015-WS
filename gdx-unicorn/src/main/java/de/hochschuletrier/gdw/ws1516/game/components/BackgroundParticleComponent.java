package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

public class BackgroundParticleComponent extends Component implements Pool.Poolable
{
    public ParticleEffect bgEffect;
    
    @Override
    public void reset()
    {
        bgEffect.dispose();
        bgEffect = null;
    }
}
