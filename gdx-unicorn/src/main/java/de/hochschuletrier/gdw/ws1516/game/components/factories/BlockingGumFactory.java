package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.BlockingGumComponent;

/**
 * Factory for the 'blocking gum' component
 * @author Eileen
 * @version 1.0
 */
public class BlockingGumFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "BlockingGum";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        BlockingGumComponent component = engine.createComponent(BlockingGumComponent.class);
        
        component.timeToDespawn = properties.getFloat("timeToDespawn");
        component.onDespawn = (blocker) -> {};
        
        entity.add(component);
    }

}
