package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.CollectableComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.SoundEmitterComponent;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent.Listener;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;

/**
 * @author phili_000
 *
 */
public class SoundSystem extends IteratingSystem implements SoundEvent.Listener, Listener {

    private static final Logger logger = LoggerFactory.getLogger(GameLogicTest.class);
    private Vector2 camera;
    private AssetManagerX assetManager;
    private Engine engine;

    public SoundSystem(Vector2 camera) {
        super(Family.all(SoundEmitterComponent.class, PositionComponent.class).get());
        this.assetManager = Main.getInstance().getAssetManager();
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        SoundEvent.register(this);
        DeathEvent.register(this);     
        this.engine = engine;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        SoundEvent.unregister(this);
        DeathEvent.unregister(this);
        this.engine = null;
    };
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SoundEmitterComponent soundEmitter = ComponentMappers.soundEmitter.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        PhysixBodyComponent body = ComponentMappers.physixBody.get(entity);
        soundEmitter.emitter.setPosition(position.x, position.y, 0);
        
        /*
         * Löschen der beendeten Sounds
         */
        SoundInstance[] instances=new SoundInstance[soundEmitter.soundInstances.size()];
        soundEmitter.soundInstances.toArray(instances);
        for (int i=0;i<instances.length;i++){
            if (instances[i].isStopped()){
                logger.debug("removed");
                soundEmitter.soundNames.remove( i );
                soundEmitter.soundInstances.remove(i);
            }
        }
        
        soundEmitter.emitter.update();
        
    }

    /**
     * von 0 bis 1 (nicht von 1 bis 2)
     * @param volume
     *      neue Lautstärke
     */
    public static void setGlobalVolume(float volume)
    {
        SoundEmitter.setGlobalVolume(volume);
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 pos = CameraSystem.getCameraPosition();
        SoundEmitter.setListenerPosition(pos.x, pos.y, 10, SoundEmitter.Mode.STEREO);
        SoundEmitter.updateGlobal();
    };

    @Override
    public void onSoundPlay(String sound, Entity entity, boolean b) {
        SoundEmitterComponent soundEmitter = ComponentMappers.soundEmitter.get(entity);
        // soundEmitter.emitter.play(assetManager.getSound(sound), b);
        // System.out.println(soundEmitter.emitter);
        if ( soundEmitter  != null )
        {
            SoundInstance soundInstance = soundEmitter.emitter.play(assetManager.getSound(sound), b);
            if (soundInstance != null) {
                soundInstance.setVolume(100f);
            }
            soundEmitter.soundInstances.add(soundInstance);
            soundEmitter.soundNames.add(sound);
            logger.debug("array {} size {}", soundEmitter.soundInstances,soundEmitter.soundInstances.size());
        }else
        {
            logger.warn("Entity {} tried to play a sound( {} ), but has no SoundEmitter.",entity,sound);
        }

    }

    @Override
    public void onSoundStop(Entity entity) {
        onSoundStop(null, entity);
    }

    /**
     * @param sound 
     *          Sound to be stopped, null stops all sounds
     * @param entity
     *          Entity which plays the sound
     */
    @Override
    public void onSoundStop(String sound,Entity entity) {
        SoundEmitterComponent soundEmitter = ComponentMappers.soundEmitter.get(entity);
        logger.debug("stop sound {}", sound);
        for (int i=0;i<soundEmitter.soundNames.size();i++){
            if (sound == null ||  soundEmitter.soundNames.get(i).equals(sound)){
                if ( soundEmitter.soundInstances != null )
                {
                    if ( soundEmitter.soundInstances.get(i).isPlaying() )
                    {
                        soundEmitter.soundInstances.get(i).stop();
                    }
                    soundEmitter.soundInstances.remove(i);
                    soundEmitter.soundNames.remove(i);
                    i--;
                }
            }
        }
    }

    @Override
    public void onDeathEvent(Entity entity) {
        Entity player =  engine.getEntitiesFor( Family.all(PlayerComponent.class).get() ).first();
        CollectableComponent collect = ComponentMappers.collectable.get(entity);
        if (player != null)
        {
            if ( collect != null )
            {
                switch(collect.type )
                {
                    case CHOCO_COIN:
                        
                        SoundEvent.emit("eat_cho", player);
                        break;
                }
            }
        }
    }
    
    
    
}
