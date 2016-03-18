package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlatformComponent;

/**
 * Listens to player - platform collisions
 * @author Eileen
 * @version 1.0
 */
public class PlayerPlatformListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PlayerPlatformListener.class);
    
    @Override
    public void beginContact(PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        Entity player = contact.getMyComponent().getEntity();
        
        //Collision handling with entity
        if (otherComponent != null &&
            otherComponent.getEntity() != null &&
            otherComponent.getEntity().getComponent(PlatformComponent.class) != null) {   //With platform       
            
            ComponentMappers.movement.get(player).isOnPlatform = true;
            ComponentMappers.movement.get(player).onPlatformBody = otherComponent.getEntity().getComponent(PhysixBodyComponent.class);
            
        }

    }
    
    @Override
    public void endContact(PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        Entity player = contact.getMyComponent().getEntity();
        
        //Collision handling with entity
        if (otherComponent != null &&
            otherComponent.getEntity() != null &&
            otherComponent.getEntity().getComponent(PlatformComponent.class) != null) {   //With platform       
            
            ComponentMappers.movement.get(player).isOnPlatform = false;
            ComponentMappers.movement.get(player).onPlatformBody = otherComponent.getEntity().getComponent(PhysixBodyComponent.class);
            
        }

    }
    
}