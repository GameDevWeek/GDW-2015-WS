package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.TextureComponent;

public class TextureComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Texture";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        TextureComponent component = engine.createComponent(TextureComponent.class);
        component.texture = assetManager.getTexture(properties.getString("texture"));
        component.flipHorizontal = properties.getBoolean("flipHorizontal", false);
        component.flipVertical = properties.getBoolean("flipVertical", false);
        component.alpha = properties.getFloat("alpha", 1.0f);
        
        entity.add(component);
    }
}