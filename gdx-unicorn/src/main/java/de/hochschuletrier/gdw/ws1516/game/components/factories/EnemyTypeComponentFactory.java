package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent.EnemyType;

public class EnemyTypeComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "EnemyType";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        EnemyTypeComponent component = engine.createComponent(EnemyTypeComponent.class);
        
        component.type = EnemyType.valueOf( properties.getString("type","hunter").toUpperCase() );

        entity.add(component);
    }

}
