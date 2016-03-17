package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1516.events.EndContactEvent;
import de.hochschuletrier.gdw.ws1516.events.HornCollisionEvent;
import de.hochschuletrier.gdw.ws1516.events.UnicornEnemyCollisionEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;

public class PlayerContactListener extends PhysixContactAdapter{
    private static final Logger logger = LoggerFactory.getLogger(PlayerContactListener.class);
//    PooledEngine engine;
    


    public void beginContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();

        if (contact.getOtherComponent() == null){
            if(ComponentMappers.movement.get(myEntity).state==State.FALLING||ComponentMappers.movement.get(myEntity).state==State.JUMPING){
                
                //for Jumps:
                if("foot".equals(contact.getMyFixture().getUserData())){
                    if(!contact.getOtherFixture().isSensor()){
                        ComponentMappers.movement.get(myEntity).state=MovementComponent.State.ON_GROUND;
                    }
                        // TODO: Platfom, killsPlayerOnContact, ...
                }
            }
            return;
        }
        Entity otherEntity=contact.getOtherComponent().getEntity();
        //for Jumps:
        if("foot".equals(contact.getMyFixture().getUserData())){       

            if(!contact.getOtherFixture().isSensor()){
                ComponentMappers.movement.get(myEntity).state=MovementComponent.State.ON_GROUND;
            }
                // TODO: Platfom, killsPlayerOnContact, ...
        }
        
        Entity playerEn=myEntity;
        Entity otherEn=otherEntity;
        if (ComponentMappers.player.has(otherEntity)&&ComponentMappers.enemyType.has(myEntity)){
            playerEn=otherEntity;
            otherEn=myEntity;
        }
        //for UnicornEnemyCollision:
        if(ComponentMappers.player.has(playerEn)&&ComponentMappers.enemyType.has(otherEn)){
            //TODO Hornattack
            PlayerComponent player=playerEn.getComponent(PlayerComponent.class);
            if(player.state==PlayerComponent.State.HORNATTACK&&"horn".equals(contact.getMyFixture().getUserData())){
                HornCollisionEvent.emit(playerEn, otherEn);
                logger.debug("hornKollision {}");
            }else{
                UnicornEnemyCollisionEvent.emit(playerEn, otherEn);
                logger.debug("gegnerKollision {}");
            }
        }
//        if(ComponentMappers.enemyType.has(myEntity)&&ComponentMappers.player.has(otherEntity)){
//            PlayerComponent player=otherEntity.getComponent(PlayerComponent.class);
//            if(player.state==PlayerComponent.State.HORNATTACK&&"horn".equals(contact.getOtherFixture().getUserData())){
//                HornCollisionEvent.emit(otherEntity, myEntity);
//                logger.debug("hornKollision {}");
//            }else{
//                UnicornEnemyCollisionEvent.emit(myEntity, otherEntity);
//                logger.debug("gegnerKollision {}");
//            }
//        }

        
        return;
    
    }
    
    public void endContact(PhysixContact contact){
        if (contact.getOtherComponent() == null){
            return;
        }
        
        Entity player = contact.getMyComponent().getEntity();
        Entity otherEntity = contact.getOtherComponent().getEntity();
        
        /**
         * @author tobi - Gamelogic
         * ein endContactEvent schmeißen
         */
        EndContactEvent.emit(player, otherEntity);
    }

}
