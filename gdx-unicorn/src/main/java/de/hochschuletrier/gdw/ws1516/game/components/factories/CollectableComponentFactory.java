package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.BackgroundParticleComponent;
import de.hochschuletrier.gdw.ws1516.game.components.CollectableComponent;

public class CollectableComponentFactory extends ComponentFactory<EntityFactoryParam>
{

    @Override
    public String getType() 
    {
        return "Collectable";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) 
    {
        CollectableComponent component = engine.createComponent(CollectableComponent.class);
        
        component.type = CollectableComponent.CollectableType.valueOf( properties.getString("type").toUpperCase() );
        
        entity.add(component);
    }


    
    
}
