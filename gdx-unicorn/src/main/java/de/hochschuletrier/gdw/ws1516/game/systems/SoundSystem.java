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
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.CollectableComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.State;
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
        MovementComponent move = ComponentMappers.movement.get(entity);
        PlayerComponent player = ComponentMappers.player.get(entity);
        
        if (move != null && player!=null)
        {
            boolean shouldPlay = false;
            if ( move.state == State.ON_GROUND )
            {
                if ( Math.abs(body.getLinearVelocity().x)>1f )
                {
                    shouldPlay = true;
                }
            }
            if( shouldPlay )
            {
                if ( !soundEmitter.soundNames.contains("laufen") )
                    SoundEvent.emit("laufen", entity,true);
            }else
            {
                if ( soundEmitter.soundNames.contains("laufen") )
                    SoundEvent.stopSound("laufen",entity);
            }
        }
        
        
        
        
        soundEmitter.emitter.setPosition(position.x, position.y, 0);
        /*
         * Löschen der beendeten Sounds
         */
        for (int i=0;i<soundEmitter.soundInstances.size();i++){
            if (soundEmitter.soundInstances.get(i).isStopped()){
                soundEmitter.soundNames.remove( i );
                soundEmitter.soundInstances.remove(i);
                i--;
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
        camera = CameraSystem.getCameraPosition();
        SoundEmitter.setListenerPosition(camera.x, camera.y, 100, SoundEmitter.Mode.STEREO);
        SoundEmitter.updateGlobal();
        super.update(deltaTime);
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
                soundInstance.setVolume(1f);
            }
            soundEmitter.soundInstances.add(soundInstance);
            soundEmitter.soundNames.add(sound);
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
        for (int i=0;i<soundEmitter.soundNames.size();i++){
            if (sound == null ||  soundEmitter.soundNames.get(i).equals(sound)){
                soundEmitter.soundInstances.get(i).stop();
                
                soundEmitter.soundInstances.remove(i);
                soundEmitter.soundNames.remove(i);
                i--;
                
            }
        }
    }

    @Override
    public void onDeathEvent(Entity entity) {
        Entity player =  engine.getEntitiesFor( Family.all(PlayerComponent.class).get() ).first();
        CollectableComponent collect = ComponentMappers.collectable.get(entity);
        EnemyTypeComponent enemy = ComponentMappers.enemyType.get(entity);
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
        if ( enemy != null )
        {

            SoundEvent.emit("paparazzidie", player);
        }
    }
    
    
    
}
