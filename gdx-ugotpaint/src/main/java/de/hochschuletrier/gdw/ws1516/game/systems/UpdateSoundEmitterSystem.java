package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundDistanceModel;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.SoundEmitterComponent;

public class UpdateSoundEmitterSystem extends IteratingSystem implements SoundEvent.Listener {

    private static final CVarEnum<SoundDistanceModel> distanceModel = new CVarEnum("snd_distanceModel", SoundDistanceModel.INVERSE, SoundDistanceModel.class, 0, "sound distance model");
    private static final CVarEnum<SoundEmitter.Mode> emitterMode = new CVarEnum("snd_mode", SoundEmitter.Mode.STEREO, SoundEmitter.Mode.class, 0, "sound mode");
    private final AssetManagerX assetManager;

    public UpdateSoundEmitterSystem() {
        super(Family.all(PositionComponent.class, SoundEmitterComponent.class).get(), 0);
        assetManager = Main.getInstance().getAssetManager();
    }

    public static void initCVars() {
        DevConsole console = Main.getInstance().console;
        console.register(distanceModel);
        distanceModel.addListener((CVar) -> distanceModel.get().activate());

        console.register(emitterMode);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        SoundEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        SoundEvent.unregister(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        SoundEmitter.setListenerPosition(GameConstants.WINDOW_WIDTH/2,
                GameConstants.WINDOW_HEIGHT/2, 10, emitterMode.get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SoundEmitterComponent sound = ComponentMappers.soundEmitter.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        sound.emitter.setPosition(position.pos.x, position.pos.y, 0);
        sound.emitter.update();
    }

    @Override
    public void onSoundEvent(Entity entity, String name) {
        SoundEmitterComponent component = ComponentMappers.soundEmitter.get(entity);
        if(component != null) {
            component.emitter.play(assetManager.getSound(name), false);
        }
    }
}
