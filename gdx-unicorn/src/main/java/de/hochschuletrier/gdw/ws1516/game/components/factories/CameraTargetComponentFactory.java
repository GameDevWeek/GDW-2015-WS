package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.CameraTargetComponent;

public class CameraTargetComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "CameraTarget";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        CameraTargetComponent component = engine.createComponent(CameraTargetComponent.class);
        entity.add(component);
    }

}
