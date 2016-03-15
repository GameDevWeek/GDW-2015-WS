package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.HitPointsComponent;

public class HitPointsComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "HP";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        HitPointsComponent hpcomp = engine.createComponent(HitPointsComponent.class);
        hpcomp.value = properties.getInt("value", 3);
        entity.add(hpcomp);
    }

}
