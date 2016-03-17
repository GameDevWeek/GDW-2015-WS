package de.hochschuletrier.gdw.ws1516.game.systems;

import java.awt.FlowLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.events.EndFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;

public class PlayerStateSystem extends IteratingSystem implements RainbowEvent.Listener,
    HornAttackEvent.Listener{

    private static final Logger logger = LoggerFactory.getLogger(PlayerStateSystem.class);
    
    public PlayerStateSystem() {
        super(Family.all(PlayerComponent.class,MovementComponent.class).get());
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        RainbowEvent.register(this);
        HornAttackEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        RainbowEvent.unregister(this);
        HornAttackEvent.unregister(this);
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent movementComp = ComponentMappers.movement.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        playerComp.stateTimer = Math.max(playerComp.stateTimer - deltaTime, 0);
        playerComp.hornAttackCooldown = Math.max(playerComp.hornAttackCooldown - deltaTime, 0);
        playerComp.invulnerableTimer = Math.max(playerComp.invulnerableTimer - deltaTime, 0);
        if (playerComp.stateTimer <= 0.0f) {
            if (playerComp.state == State.HORNATTACK) {
                HornAttackEvent.stop(entity);
            }
            if (playerComp.state==State.RAINBOW){
                RainbowEvent.end(entity);
                movementComp.speed=GameConstants.PLAYER_SPEED;
            } 
            playerComp.state = State.NORMAL;
        }
    }
    
    @Override
    public void onRainbowCollect(Entity player) {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            MovementComponent movementComp = ComponentMappers.movement.get(getEntities().get(0));
            if (playerComp.state==State.HORNATTACK){
                HornAttackEvent.stop(player);
            }
            EndFlyEvent.emit(player);
            playerComp.state=State.RAINBOW;
            playerComp.stateTimer=GameConstants.RAINBOW_MODE_TIME;
            movementComp.speed=GameConstants.PLAYER_SPEED*GameConstants.RAINBOW_SPEED_MODIFIER;
        }
        
    }
    
    @Override
    public void onHornAttackStart(Entity player) {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.state = State.HORNATTACK;
            playerComp.stateTimer = GameConstants.HORN_MODE_TIME;
            playerComp.hornAttackCooldown = GameConstants.HORN_MODE_TIME;
            
        }
    }
    
     
    public void onHornAttackStop(Entity player) {

        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.hornAttackCooldown = GameConstants.HORN_MODE_COOLDOWN;
        }
    }
    


    
    @Override
    public void onRainbowModeEnd(Entity player) {
        if (getEntities().size()>0){
        PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
        playerComp.hornAttackCooldown = GameConstants.HORN_MODE_COOLDOWN;
        }
    }
    

    
}
