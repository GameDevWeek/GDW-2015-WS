package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.State;

public class PlayerContactListener extends PhysixContactAdapter{
    private static final Logger logger = LoggerFactory.getLogger(PlayerContactListener.class);
//    PooledEngine engine;
    


    public void beginContact(PhysixContact contact) {
        Entity player = contact.getMyComponent().getEntity();
        if (contact.getOtherComponent() == null){
            if(ComponentMappers.movement.get(player).state==State.FALLING||ComponentMappers.movement.get(player).state==State.JUMPING){
                
                //for Jumps:
                if("foot".equals(contact.getMyFixture().getUserData())){       

                    if(!contact.getOtherFixture().isSensor()){
                        ComponentMappers.movement.get(player).state=MovementComponent.State.ON_GROUND;
                    }
                        // TODO: Platfom, killsPlayerOnContact, ...
                }
            }

            return;
        }
        Entity otherEntity = contact.getOtherComponent().getEntity();
        
        //for Jumps:
        if("foot".equals(contact.getMyFixture().getUserData())){       

            if(!contact.getOtherFixture().isSensor()){
                ComponentMappers.movement.get(player).state=MovementComponent.State.ON_GROUND;
            }
                // TODO: Platfom, killsPlayerOnContact, ...
        }
        return;
    
    }
    
    public void endContact(PhysixContact contact){
        if (contact.getOtherComponent() == null){
            return;
        }
        
        Entity player = contact.getMyComponent().getEntity();
        Entity otherEntity = contact.getOtherComponent().getEntity();
        
    }

}
