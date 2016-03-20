package de.hochschuletrier.gdw.ws1516.game.components.factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.UnicornAnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.UnicornAnimationComponent.UnicornColor;

public class UnicornAnimationComponentFactory extends AnimationComponentFactory {
    
    @Override
    public String getType() {
        return "UnicornAnimation";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) 
    {
        UnicornAnimationComponent component = engine.createComponent(UnicornAnimationComponent.class);
        component.unicornColoredAnimations = new HashMap<>();
        
        for(UnicornAnimationComponent.UnicornColor color : UnicornAnimationComponent.UnicornColor.values())
        {
            HashMap<String, AnimationExtended> map = new HashMap<>();
            for(MovementComponent.State animState : MovementComponent.State.values())
            {
                if(animState == MovementComponent.State.ON_GROUND)
                {
                    String idleString = properties.getString(color.toString() + "_" + animState.toString().toLowerCase() + "_idle");
                    String walkingString = properties.getString(color.toString() + "_" + animState.toString().toLowerCase() + "_walking");
                    
                    if(idleString != null)
                    {
                        map.put(animState.toString().toLowerCase() + "_idle", assetManager.getAnimation(idleString));
                    }
                    if(idleString != null)
                    {
                        map.put(animState.toString().toLowerCase() + "_walking", assetManager.getAnimation(walkingString));
                    }
                }
                else
                {
                    String stateString = properties.getString(color.toString() + "_" + animState.toString().toLowerCase());
                    
                    if(stateString != null)
                    {
                        map.put(animState.toString().toLowerCase(), assetManager.getAnimation(stateString));
                    }
                }
            }
            
            // add dash
            String dashString = properties.getString(color.toString() + "_dash");
            if(dashString != null)
            map.put("dash", assetManager.getAnimation(dashString));
            
            component.unicornColoredAnimations.put(color.toString(), map);
        }
        
        component.switchUnicornColor(UnicornColor.pink);
        
        fillAnimationComponent(component, properties);
        
        entity.add(component);
    }
}