package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.BulletComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.BubblegumSpitSystem;

/**
 * Listens to bullet collisions
 * @author Eileen
 * @version 1.0
 */
public class BulletListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BulletListener.class);
    
    @Override
    public void beginContact(final PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        Entity myEntity = contact.getMyComponent().getEntity();
        BulletComponent bulletComponent = ComponentMappers.bulletComponent.get(myEntity);
        
        //Collision handling with entity
        if (otherComponent != null) {
            if (otherComponent.getEntity() != null) {
                if (otherComponent.getEntity().getComponent(PlayerComponent.class) != null) {   //With player       
                    bulletComponent.onPlayerHit.accept(myEntity, otherComponent.getEntity());                 
                } else {
                    bulletComponent.onEntityHit.accept(myEntity, otherComponent.getEntity());   //Something else
                }
            }               
        }
        
        //OnHit
        bulletComponent.onHit.accept(myEntity);
        
    }
    
}