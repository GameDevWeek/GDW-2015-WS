package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;

public class PathComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Path";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        PathComponent component = engine.createComponent(PathComponent.class);

        entity.add(component);
    }

}
