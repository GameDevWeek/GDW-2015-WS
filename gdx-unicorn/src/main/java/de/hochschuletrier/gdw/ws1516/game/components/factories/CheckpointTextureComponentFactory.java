package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.CheckpointTextureComponent;

public class CheckpointTextureComponentFactory extends TextureComponentFactory {
    @Override
    public String getType() {
        return "CheckpointTexture";
    }
    
    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        CheckpointTextureComponent component = engine.createComponent(CheckpointTextureComponent.class);
        fillTextureComponent(component, properties);
        component.activatedTexture = assetManager.getTexture(properties.getString("actived"));
        component.deactivatedTexture = assetManager.getTexture(properties.getString("deactivated"));
        entity.add(component);
    }
}