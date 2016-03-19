package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.BubblegumGlueSpawnEvent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;

/**
 * Listens to blocking gum collisions
 * @author Eileen
 * @version 1.0
 */
public class BlockingGumListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BlockingGumListener.class);
    
    @Override
    public void beginContact(final PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        Entity myEntity = contact.getMyComponent().getEntity();

        //Collision handling with entity
        if (otherComponent != null) {
            if (otherComponent.getEntity() != null) {
                if (otherComponent.getEntity().getComponent(EnemyTypeComponent.class) != null) {   //With enemy       
                    BubblegumGlueSpawnEvent.emit(myEntity, otherComponent.getEntity());         
                }
            }               
        }
        
    }
    
}