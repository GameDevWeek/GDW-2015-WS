package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

public class ParticleTestComponent extends Component implements Pool.Poolable
{
    public ParticleEffect effect;
    
    @Override
    public void reset()
    {
        effect.dispose();
        effect = null;
    }
}
