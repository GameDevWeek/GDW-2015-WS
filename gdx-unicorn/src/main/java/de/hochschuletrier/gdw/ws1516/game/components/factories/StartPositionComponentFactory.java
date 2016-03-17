package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;

public class StartPositionComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "StartPosition";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        StartPointComponent component = engine.createComponent(StartPointComponent.class);

        entity.add(component);
    }

}
