package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import de.hochschuletrier.gdw.commons.utils.pool.Poolable;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class ParticleComponent extends Component implements Poolable{

    public ParticleEffect effect;

    @Override
    public void reset() {
        effect = null;
    }
}
