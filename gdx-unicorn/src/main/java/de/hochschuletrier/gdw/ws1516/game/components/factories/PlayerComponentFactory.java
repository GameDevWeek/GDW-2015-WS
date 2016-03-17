package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;

public class PlayerComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Player";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        PlayerComponent component = engine.createComponent(PlayerComponent.class);
        
        component.maxHitpoints = properties.getInt("maxHitpoints",3);
        component.hitpoints = properties.getInt("hitpoints",3);
        component.lives = properties.getInt("lives",3);

        entity.add(component);
    }
}