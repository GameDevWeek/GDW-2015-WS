package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;

public class ScoreComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "ScoreBoard";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        ScoreComponent component = engine.createComponent(ScoreComponent.class);

        entity.add(component);
    }

}
