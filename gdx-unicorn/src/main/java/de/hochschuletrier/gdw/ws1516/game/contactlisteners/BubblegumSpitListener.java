package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.BubblegumSpitComponent;
import de.hochschuletrier.gdw.ws1516.game.components.BulletComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlatformComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;

/**
 * Listens to bubble-gum spit collisions
 * @author Eileen
 * @version 1.0
 */
public class BubblegumSpitListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BubblegumSpitListener.class);
    
    /**
     * Contains details about a collision
     * @author Eileen
     * @version 1.0
     */
    public static class CollisionDetails {
        public boolean hitEnemy;
        public Vector2 normal;
    };
    
    @Override
    public void beginContact(final PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        Entity myEntity = contact.getMyComponent().getEntity();
        BubblegumSpitComponent gumSpit = ComponentMappers.bubblegumSpitComponent.get(myEntity);
                
        //Test enemy hit
        boolean hitEnemy = false;
        
        //Collision handling with entity
        if (otherComponent != null) {
            if (otherComponent.getEntity() != null) {
                //Collide with enemies
                if (otherComponent.getEntity().getComponent(EnemyTypeComponent.class) != null) {          
                    gumSpit.onEnemyHit.accept(myEntity, otherComponent.getEntity());
                    hitEnemy = true;
                }
            }               
        }

        //Non spitable entitys
        if (otherComponent != null &&
            otherComponent.getEntity() != null) {
            
            //Remove spit if it hits a platform
            if (otherComponent.getEntity().getComponent(PlatformComponent.class) != null)
                DeathEvent.emit(myEntity);
            
            //Remove spit if it hits a bullet
            if (otherComponent.getEntity().getComponent(BulletComponent.class) != null) {
                DeathEvent.emit(myEntity);
                DeathEvent.emit(otherComponent.getEntity());
            }
                
            return;
        }
        
        //OnHit (ignore player hits)
        if (otherComponent == null ||
            otherComponent.getEntity() == null ||
            otherComponent.getEntity().getComponent(PlayerComponent.class) == null) {      
            
                //Create details
                CollisionDetails details = new CollisionDetails();
                details.hitEnemy = hitEnemy;
                details.normal = contact.getWorldManifold().getNormal();
            
                gumSpit.onHit.accept(myEntity, details);
        }
        
    }

}
