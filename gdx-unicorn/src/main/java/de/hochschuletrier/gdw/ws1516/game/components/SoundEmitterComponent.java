package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;


public class SoundEmitterComponent extends Component implements Poolable {

    public final SoundEmitter emitter = new SoundEmitter();
    
    public boolean isPlaying;
    
    @Override
    public void reset() {
        emitter.dispose();
        isPlaying = false;
    }

}
