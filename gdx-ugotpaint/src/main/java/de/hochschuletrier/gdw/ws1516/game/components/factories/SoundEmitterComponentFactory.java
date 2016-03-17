package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.SoundEmitterComponent;

public class SoundEmitterComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "SoundEmitter";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        entity.add(engine.createComponent(SoundEmitterComponent.class));
    }
}