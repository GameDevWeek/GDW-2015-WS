package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.DummyUnicornComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ImpactSoundComponent;

public class DummyUnicornComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "DummyUnicorn";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        DummyUnicornComponent component = engine.createComponent(DummyUnicornComponent.class);
        entity.add(component);
    }

}
