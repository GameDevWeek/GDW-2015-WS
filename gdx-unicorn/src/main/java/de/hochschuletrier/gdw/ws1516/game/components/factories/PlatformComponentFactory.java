package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.PlatformComponent;

public class PlatformComponentFactory extends ComponentFactory<EntityFactoryParam> 
{

        @Override
        public String getType() {
            return "Platform";
        }

        @Override
        public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
            PlatformComponent component = engine.createComponent(PlatformComponent.class);
            String identifier = properties.getString("mode");
//          System.out.println("MovementComponentFactory " + identifier);
          if(identifier != null)
          {
              switch(identifier){
                  case"one_time":
                      component.mode=PlatformComponent.PlatformMode.ONE_TIME;
                      break;
                  case"always":
                      component.mode=PlatformComponent.PlatformMode.ALWAYS;
                  default:
                      component.mode=PlatformComponent.PlatformMode.PAUSE;             
              }   
          }
            entity.add(component);
        }
    }