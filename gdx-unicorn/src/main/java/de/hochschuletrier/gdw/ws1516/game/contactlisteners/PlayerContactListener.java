package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.EndContactEvent;
import de.hochschuletrier.gdw.ws1516.events.HealEvent;
import de.hochschuletrier.gdw.ws1516.events.HornCollisionEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent.ScoreType;
import de.hochschuletrier.gdw.ws1516.events.UnicornEnemyCollisionEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.CollectableComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;

public class PlayerContactListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PlayerContactListener.class);
    // PooledEngine engine;
    
    //contact count for player/world contact fix jumping bug only works with singlePlayer
//    int contactCount=0;
    public void beginContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();
       MovementComponent movementComp=ComponentMappers.movement.get(myEntity);
       if(movementComp!=null){
           movementComp.contacts.add(contact.getOtherFixture());
       }
        if (contact.getOtherComponent() == null) {
        // if (ComponentMappers.player.has(myEntity)){
        // contactCount++;
        // }
            if (ComponentMappers.movement.get(myEntity).state == State.ON_GROUND || ComponentMappers.movement.get(myEntity).state == State.LANDING) {                
                // for Jumps:
                if ("foot".equals(contact.getMyFixture().getUserData())) {
                    logger.debug("sollte nicht so sein");
                }
            }
            if (ComponentMappers.movement.get(myEntity).state == State.FALLING || ComponentMappers.movement.get(myEntity).state == State.JUMPING) {                
                // for Jumps:
                if ("foot".equals(contact.getMyFixture().getUserData())) {
                    if (!contact.getOtherFixture().isSensor()) {
                        logger.debug("landing{}");
                        MovementComponent.State oldState = ComponentMappers.movement.get(myEntity).state;
                        MovementComponent.State newState = MovementComponent.State.LANDING;
                        MovementStateChangeEvent.emit(myEntity, oldState, newState);
                        ComponentMappers.movement.get(myEntity).state = newState;
                    }
                }
            }
            return;
        }
        Entity otherEntity = contact.getOtherComponent().getEntity();
        // for Jumps:
//        if ("foot".equals(contact.getMyFixture().getUserData())) {
//            if (!contact.getOtherFixture().isSensor()) {
//                logger.debug("landing{}");
//                MovementComponent.State oldState = ComponentMappers.movement.get(myEntity).state;
//                MovementComponent.State newState = MovementComponent.State.LANDING;
//                MovementStateChangeEvent.emit(myEntity, oldState, newState);
//                ComponentMappers.movement.get(myEntity).state = newState;
//            }
//            // TODO: Platfom, killsPlayerOnContact, ...
//        }
        
        Entity playerEn = myEntity;
        Entity otherEn = otherEntity;
        if (ComponentMappers.player.has(otherEntity) && ComponentMappers.enemyType.has(myEntity)) {
            playerEn = otherEntity;
            otherEn = myEntity;
        }
        // for UnicornEnemyCollision:
        if (ComponentMappers.player.has(playerEn) && ComponentMappers.enemyType.has(otherEn)) {
            // TODO Hornattack
            PlayerComponent player = playerEn.getComponent(PlayerComponent.class);
            if (player.state == PlayerComponent.State.HORNATTACK && "horn".equals(contact.getMyFixture().getUserData())) {
                HornCollisionEvent.emit(playerEn, otherEn);
                //logger.debug("hornKollision {}");
            } else {
                UnicornEnemyCollisionEvent.emit(playerEn, otherEn);
                //logger.debug("gegnerKollision {}");
            }
        }
        /**
         * @author Tobi - GameLogic
         * einsammeln von Items
         */
        if(ComponentMappers.player.has(playerEn)&&ComponentMappers.collectable.has(otherEn)){
            PlayerComponent player=playerEn.getComponent(PlayerComponent.class);
            CollectableComponent collect = ComponentMappers.collectable.get(otherEn);
            StartPointComponent start = ComponentMappers.startPoint.get(playerEn);
            PositionComponent collectPos = ComponentMappers.position.get(otherEn);
            
            if ( !collect.isCollected )
            {
                switch (collect.type) 
                {
                    case RAINBOW_GUM:
                            RainbowEvent.start(playerEn);
                            DeathEvent.emit(otherEn); 
                        break;
                    case BLUE_GUM:
                            player.blueGumStacks++;
                            DeathEvent.emit(otherEn);  
                        break;
                    case CHOCO_COIN:
                            ScoreBoardEvent.emit(ScoreType.CHOCO_COIN, 1);
                            DeathEvent.emit(otherEn);
                        break;
                    case BONBON:
                            ScoreBoardEvent.emit(ScoreType.BONBON, 1);
                            DeathEvent.emit(otherEn);                            
                    break;
                    case SPAWN_POINT:
                            start.x = collectPos.x;
                            start.y = collectPos.y;         
                            
                            /// TODO Change animation
                            HealEvent.emit(playerEn, player.maxHitpoints);
                        break;                        
                    default:
                        break;
                }                
                collect.isCollected = true;
            }
          
        }
        // if(ComponentMappers.enemyType.has(myEntity)&&ComponentMappers.player.has(otherEntity)){
        // PlayerComponent
        // player=otherEntity.getComponent(PlayerComponent.class);
        // if(player.state==PlayerComponent.State.HORNATTACK&&"horn".equals(contact.getOtherFixture().getUserData())){
        // HornCollisionEvent.emit(otherEntity, myEntity);
        // logger.debug("hornKollision {}");
        // }else{
        // UnicornEnemyCollisionEvent.emit(myEntity, otherEntity);
        // logger.debug("gegnerKollision {}");
        // }
        // }
        
        return;
        
    }
    
    public void endContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();
        
        MovementComponent movementComp=ComponentMappers.movement.get(myEntity);
        if(movementComp!=null){
            movementComp.contacts.remove(contact.getOtherFixture());
            logger.debug("Verlassen Kontakte:{}",movementComp.contacts.size());
        }
        
        if (contact.getOtherComponent() == null) {
//            if (ComponentMappers.player.has(myEntity)){
//                contactCount--;
//            }
            if (ComponentMappers.movement.get(myEntity).state == State.ON_GROUND&&movementComp.contacts.isEmpty() ) {                
                // for Jumps:
//                if ("foot".equals(contact.getMyFixture().getUserData())) {
                    if (!contact.getOtherFixture().isSensor()) {
                        logger.debug("falling{}");
                        MovementComponent.State oldState = ComponentMappers.movement.get(myEntity).state;
                        MovementComponent.State newState = MovementComponent.State.FALLING;
                        MovementStateChangeEvent.emit(myEntity, oldState, newState);
                        ComponentMappers.movement.get(myEntity).state = newState;
//                    }
                }
            }
            return;
        }
        
        Entity otherEntity = contact.getOtherComponent().getEntity();
        
        /**
         * @author tobi - Gamelogic ein endContactEvent schmeißen
         */
        EndContactEvent.emit(myEntity, otherEntity);
    }

}
