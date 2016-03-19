package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent;
import de.hochschuletrier.gdw.ws1516.events.HealEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.UnicornEnemyCollisionEvent;
import de.hochschuletrier.gdw.ws1516.events.HornCollisionEvent;
import de.hochschuletrier.gdw.ws1516.events.ThrowBackEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.LookDirection;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;

public class HitPointManagementSystem extends EntitySystem implements HitEvent.Listener, DeathEvent.Listener, HealEvent.Listener, UnicornEnemyCollisionEvent.Listener, HornCollisionEvent.Listener {

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
    public void onHitEvent(Entity wasHit, Entity source, int value) {
        if (value > 0 )
        {
            PlayerComponent playerComp = pm.get(wasHit);
            if (playerComp == null) //wenn es sich um einen Gegner handelt wars das soweit.
                return;
            
            if (playerComp.state!=PlayerComponent.State.RAINBOW && playerComp.invulnerableTimer==0){
                playerComp.hitpoints-= value;
                playerComp.invulnerableTimer=GameConstants.INVULNERABLE_TIMER;
                
                if (playerComp.hitpoints <= 0)
                    DeathEvent.emit(wasHit);
                
                PhysixBodyComponent unicorn = ComponentMappers.physixBody.get(wasHit);
                PhysixBodyComponent enemy = ComponentMappers.physixBody.get(source);
                
                if (unicorn.getX() <= enemy.getX()) {
                    ThrowBackEvent.start(wasHit, -1.0f);
                } else {
                    ThrowBackEvent.start(wasHit, 1.0f);
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


        /*
         * kann nicht sterben während es am respawnen ist, 
         * oder der RainBowMode active ist
         */
        if (playerComp.state!=PlayerComponent.State.RAINBOW && !playerComp.doRespawn){   
            //es handelt sich um das Einhorn, also Leben abziehen.
            if (playerComp.lives>0){
                playerComp.lives--;
                
                if (playerComp.lives > 0)
                    GameRespawnEvent.emit();
                else
                    GameOverEvent.emit(false);
            }
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
        PlayerComponent player=ComponentMappers.player.get(unicorn);
        if (player.state==State.RAINBOW){
            DeathEvent.emit(enemy);
        }else{
            HitEvent.emit(unicorn, enemy, 1);
        }
    }

    @Override
    public void onHornCollisionEvent(Entity unicorn, Entity enemy) {
        DeathEvent.emit(enemy);
    }
    
    @Override
    public void update(float deltaTime) {
        Entity unicorn = engine.getEntitiesFor(Family.all(PlayerComponent.class, AnimationComponent.class).get()).first();
        
        if (unicorn != null) {
            PlayerComponent playerComp = ComponentMappers.player.get(unicorn);
            AnimationComponent animComp = ComponentMappers.animation.get(unicorn);
            
            if (playerComp != null && animComp != null) {
                
                if (playerComp.throwBackCooldown > 0) {
                    animComp.alpha = (float) (Math.sin(GameConstants.THROWBACK_ANIMATION_PERIODS * 2.0 * Math.PI *  (double) (playerComp.throwBackCooldown / GameConstants.THROWBACK_MODE_COOLDOWN)) + 1.25f) * 0.375f;//spuckt werte alpha werte zwischen 0.25 und 1.0 aus
                } else {
                    animComp.alpha = 1.0f;
                }
            }
        }
    }
}
