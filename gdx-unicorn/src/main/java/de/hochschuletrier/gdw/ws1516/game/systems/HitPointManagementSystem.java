package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent;
import de.hochschuletrier.gdw.ws1516.events.HealEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent.HitType;
import de.hochschuletrier.gdw.ws1516.events.UnicornEnemyCollisionEvent;
import de.hochschuletrier.gdw.ws1516.events.HornCollisionEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;

public class HitPointManagementSystem extends EntitySystem implements HitEvent.Listener, DeathEvent.Listener, HealEvent.Listener, UnicornEnemyCollisionEvent.Listener, HornCollisionEvent.Listener{

    private static final Logger logger = LoggerFactory.getLogger(HitPointManagementSystem.class);
    
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
    private Engine engine;
    
    @Override
    public void addedToEngine(Engine engine) {
        HitEvent.register(this);
        DeathEvent.register(this);
        HealEvent.register(this);
        UnicornEnemyCollisionEvent.register(this);
        HornCollisionEvent.register(this);
        this.engine = engine;
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        HitEvent.unregister(this);
        DeathEvent.unregister(this);
        HealEvent.unregister(this);
        UnicornEnemyCollisionEvent.unregister(this);
        HornCollisionEvent.unregister(this);
        this.engine = null;
    }
    
    @Override
    public void onHitEvent(Entity entity, HitType type, int value) {
        if (value > 0 )
        {
            PlayerComponent playerComp = pm.get(entity);
            if (playerComp == null) //wenn es sich um einen Gegner handelt wars das soweit.
                return;
            if (playerComp.state!=PlayerComponent.State.RAINBOW && playerComp.invulnerableTimer==0){
                playerComp.hitpoints-= value;
                playerComp.invulnerableTimer=GameConstants.INVULNERABLE_TIMER;
                
                if (playerComp.hitpoints <= 0)
                    DeathEvent.emit(entity);
                
                
                //ansonsten war es das Einhorn => unterschiedliches Verhalten, je nach Art des HitPoints.
                switch (type) {
                case TOUCH:
                    //TODO anpassen!
                    //testweise vektoren in abhaengigkeit der Blickrichtung des Einhorns
                    MovementComponent moveComp = mm.get(entity);
                    float forceX = 0.0f;
                    float forceY = -0.5f;
                    
                    if (moveComp.lookDirection == MovementComponent.LookDirection.LEFT) {
                        forceX = 1.0f;
                    } else {
                        forceX = -1.0f;
                    }
                    
                    break;
                default:
                    break;
                }
           }
        } else
        {

            logger.warn("cannot damage for {} Hitpoints",value);
        }
    }

    @Override
    public void onDeathEvent(Entity entity) {
        PlayerComponent playerComp = pm.get(entity);

        if (playerComp == null) { //es handelt sich also um einen Gegner und nicht um das Einhorn, also Gegner entfernen.
            engine.removeEntity(entity);
            return;
        }

        if (playerComp.state!=PlayerComponent.State.RAINBOW){   
            //es handelt sich um das Einhorn, also Leben abziehen.
            playerComp.lives--;
            
            if (playerComp.lives > 0)
                GameRespawnEvent.emit();
            else
                GameOverEvent.emit();
        }
    }


    @Override
    public void onHealEvent(Entity entity, int value) {
        if (value > 0 )
        {
            PlayerComponent playerComp = ComponentMappers.player.get(entity);
            if ( playerComp != null ) 
            {
                playerComp.hitpoints = Math.min(playerComp.hitpoints+value,playerComp.maxHitpoints);
            }
        }else
        {
            logger.warn("cannot heal for {} Hitpoints",value);
        }
    }

    @Override
    public void onUnicornEnemyCollisionEvent(Entity unicorn, Entity enemy) {
        HitEvent.emit(unicorn, HitType.TOUCH, 1);
    }

    @Override
    public void onHornCollisionEvent(Entity unicorn, Entity enemy) {
        DeathEvent.emit(enemy);
    }
}
