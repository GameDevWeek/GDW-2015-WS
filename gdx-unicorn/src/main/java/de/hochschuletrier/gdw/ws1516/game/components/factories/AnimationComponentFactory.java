package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import javafx.scene.input.KeyCombination.ModifierValue;

public class AnimationComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Animation";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) 
    {
        AnimationComponent component = engine.createComponent(AnimationComponent.class);
                
        for(MovementComponent.State animState : MovementComponent.State.values())
        {
            if(animState == MovementComponent.State.ON_GROUND)
            {
                String idleString = properties.getString(animState.toString().toLowerCase() + "_idle");
                String walkingString = properties.getString(animState.toString().toLowerCase() + "_walking");
                
                component.animationMap.put(animState.toString().toLowerCase() + "_idle", assetManager.getAnimation(idleString));
                component.animationMap.put(animState.toString().toLowerCase() + "_walking", assetManager.getAnimation(walkingString));                
            }
            else
            {
                String stateString = properties.getString(animState.toString().toLowerCase());
                
                if(stateString != null)
                {
                    component.animationMap.put(animState.toString().toLowerCase(), assetManager.getAnimation(stateString));
                }
            }
        }
        
        fillAnimationComponent(component, properties);
        
        entity.add(component);
    }
    
    protected void fillAnimationComponent(AnimationComponent component, SafeProperties properties)
    {
        component.flipHorizontal = properties.getBoolean("flip_horizontal", false);
        component.name = properties.getString("name", "");
        component.xOffset = properties.getFloat("x_offset", 0f);
        component.yOffset = properties.getFloat("y_offset", 0f);
        component.alpha = properties.getFloat("alpha", 1.0f);
    }
}