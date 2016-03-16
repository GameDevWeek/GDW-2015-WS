package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.BubblegumSpitComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyComponent;

/**
 * Listens to bubble-gum spit collisions
 * @author Eileen
 * @version 1.0
 */
public class BubblegumSpitListener extends PhysixContactAdapter {

    @Override
    public void beginContact(final PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        Entity myEntity = contact.getMyComponent().getEntity();
        BubblegumSpitComponent gumSpit = ComponentMappers.bubblegumSpitComponent.get(myEntity);
        
        //Collision handling with entity
        if (otherComponent != null) {
            if (otherComponent.getEntity() != null) {
                if (otherComponent.getEntity().getComponent(EnemyComponent.class) != null) {   //With enemy       
                    gumSpit.onEnemyHit.accept(myEntity, otherComponent.getEntity());                 
                }
            }               
        }
        
        //OnHit
        gumSpit.onHit.accept(myEntity);
        
    }

}
