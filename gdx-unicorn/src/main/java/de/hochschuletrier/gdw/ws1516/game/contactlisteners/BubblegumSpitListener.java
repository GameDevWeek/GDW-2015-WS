package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.BubblegumSpitComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.BubblegumSpitSystem;

/**
 * Listens to bubble-gum spit collisions
 * @author Eileen
 * @version 1.0
 */
public class BubblegumSpitListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BubblegumSpitListener.class);
    
    @Override
    public void beginContact(final PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        Entity myEntity = contact.getMyComponent().getEntity();
        BubblegumSpitComponent gumSpit = ComponentMappers.bubblegumSpitComponent.get(myEntity);
           
        //Collision handling with entity
        if (otherComponent != null) {
            if (otherComponent.getEntity() != null) {
                //Collide with enemies
                if (otherComponent.getEntity().getComponent(EnemyTypeComponent.class) != null) {          
                    gumSpit.onEnemyHit.accept(myEntity, otherComponent.getEntity());                 
                }
            }               
        }
                
        //OnHit (ignore player hits)
        if (otherComponent == null ||
            otherComponent.getEntity() == null ||
            otherComponent.getEntity().getComponent(PlayerComponent.class) == null) {       
                gumSpit.onHit.accept(myEntity);
        }
        
    }

}
