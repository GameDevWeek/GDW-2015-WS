package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent.AnimationState;

public class AnimationComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Animation";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) 
    {
        AnimationComponent component = engine.createComponent(AnimationComponent.class);
        
        for(AnimationState animState : AnimationState.values())
        {
            if(properties.getString(animState.toString()) != null)
            {
                component.animationMap.put(animState, assetManager.getAnimation(properties.getString(animState.toString())));
            }
        }
        
        if(properties.getString("animation") != null)
        {
            component.animationMap.put(AnimationState.none, assetManager.getAnimation(properties.getString("animation")));
        }
        
        component.flipHorizontal = properties.getBoolean("flipHorizontal", false);
        
        entity.add(component);
    }
}