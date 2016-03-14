package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.RenderLayerComponent;

public class RenderLayerComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "RenderLayer";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        RenderLayerComponent component = engine.createComponent(RenderLayerComponent.class);
        component.layer = properties.getInt("layer", 0);
        entity.add(component);
    }
}