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
                String animStateString = properties.getString(animState.toString().toLowerCase() + "_idle");
                String animStateString2 = properties.getString(animState.toString().toLowerCase() + "_walking");

                System.out.println("put ON_GROUND " + animState.toString().toLowerCase() + " -- " + animStateString + " - " + animStateString2);
                
                component.animationMap.put(animState.toString().toLowerCase() + "_idle", assetManager.getAnimation(animStateString));
                component.animationMap.put(animState.toString().toLowerCase() + "_walking", assetManager.getAnimation(animStateString2));                
            }
            else
            {
                String animStateString = properties.getString(animState.toString().toLowerCase());
                
                if(animStateString != null)
                {
                    System.out.println("put NOTGROUND " + animState.toString().toLowerCase() +" -- "+ animStateString);
                    
                    component.animationMap.put(animState.toString().toLowerCase(), assetManager.getAnimation(animStateString));
                }
            }
            
        }
        
        component.flipHorizontal = properties.getBoolean("flipHorizontal", false);
        
        entity.add(component);
        System.out.println("--");
    }
}