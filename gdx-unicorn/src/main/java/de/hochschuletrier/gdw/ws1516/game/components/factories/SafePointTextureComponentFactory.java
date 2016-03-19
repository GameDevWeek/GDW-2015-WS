package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.SafePointTextureComponent;

public class SafePointTextureComponentFactory extends TextureComponentFactory {
    @Override
    public String getType() {
        return "CheckpointTexture";
    }
    
    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        SafePointTextureComponent component = engine.createComponent(SafePointTextureComponent.class);
        fillTextureComponent(component, properties);
        component.activatedTexture = assetManager.getTexture(properties.getString("actived"));
        component.deactivatedTexture = assetManager.getTexture(properties.getString("deactivated"));
        entity.add(component);
    }
}