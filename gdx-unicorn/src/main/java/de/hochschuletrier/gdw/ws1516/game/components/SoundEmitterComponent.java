package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;


public class SoundEmitterComponent extends Component implements Poolable {

    public final SoundEmitter emitter = new SoundEmitter();
    
    public ArrayList<SoundInstance> soundInstances = new ArrayList<>();
    public ArrayList<String> soundNames =new ArrayList<>();
    
    public boolean isPlaying;
    
    @Override
    public void reset() {
        emitter.dispose();
        for (SoundInstance instance: soundInstances){
            instance.stop();
        }
        soundInstances.clear();
        soundNames.clear();
        isPlaying = false;
    }

}
