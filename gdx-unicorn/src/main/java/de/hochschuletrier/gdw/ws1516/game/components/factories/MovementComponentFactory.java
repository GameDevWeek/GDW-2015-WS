package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;

public class MovementComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Movable";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        MovementComponent component = engine.createComponent(MovementComponent.class);
        String identifier = properties.getString("state");
        switch(identifier){
            case"falling":
                component.state=MovementComponent.State.FALLING;
                break;
            case"flying":
                component.state=MovementComponent.State.FLYING;
                break;
            case"on_ground":
                //fallthrought intended
            default:
                component.state=MovementComponent.State.ON_GROUND;             
            
        }
        entity.add(component);
    }
}