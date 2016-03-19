package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

public class BackgroundParticleComponent extends Component implements Pool.Poolable
{
    public ParticleEffect effect;
    public float[] startEmissionHighMin;
    public float[] startEmissionHighMax;
    public float[] startEmissionLowMin;
    public float[] startEmissionLowMax;
    public boolean isFlippedHorizontal;
    public boolean isFlippedVertical;
    public boolean flipVertical;
    public boolean reduceEmissionIfIdle;
    public float offsetWhenMoving;
    public boolean killWhenFinished;
    
    @Override
    public void reset()
    {
        effect.dispose();
        effect = null;
        startEmissionHighMin = null;
        startEmissionHighMax = null;
        startEmissionLowMin = null;
        startEmissionLowMax = null;
        isFlippedHorizontal = false;
        reduceEmissionIfIdle = false;
        offsetWhenMoving = 0f;
        killWhenFinished = false;
    }
}
