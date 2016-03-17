package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.PlayerContactListener;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.events.SpuckChargeEvent;

public class PlayerStateSystem extends IteratingSystem implements RainbowEvent.Listener,
    HornAttackEvent.Listener,SpuckChargeEvent.Listener{

    private static final Logger logger = LoggerFactory.getLogger(PlayerStateSystem.class);
    
    public PlayerStateSystem() {
        super(Family.all(PlayerComponent.class).get());
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
        PlayerComponent playerComp=ComponentMappers.player.get(entity);
        playerComp.stateTimer=Math.max(playerComp.stateTimer-deltaTime, 0);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        playerComp.stateTimer = Math.max(playerComp.stateTimer - deltaTime, 0);
        if (playerComp.stateTimer <= 0.0f) {
            if (playerComp.state == State.HORNATTACK) {
                HornAttackEvent.stop(entity);
        PlayerComponent playerComp=ComponentMappers.player.get(entity);
        playerComp.stateTimer=Math.max(playerComp.stateTimer-deltaTime, 0);
        playerComp.hornAttackCooldown=Math.max(playerComp.hornAttackCooldown-deltaTime, 0);
        playerComp.spuckChargeCooldown=Math.max(playerComp.spuckChargeCooldown-deltaTime, 0);
        playerComp.invulnerableTimer=Math.max(playerComp.invulnerableTimer-deltaTime, 0);
            }
            if (playerComp.state == State.RAINBOW) {
                RainbowEvent.end(entity);
            }else if (playerComp.state==State.SPUCKCHARGE){
                SpuckChargeEvent.stop();
            }else if(playerComp.state == State.RAINBOW) {
                RainbowEvent.end(entity);
            }
            playerComp.state = State.NORMAL;
        }
    }
    
    @Override
    public void onRainbowCollect(Entity player) {
        PlayerComponent playerComp = ComponentMappers.player.get(player);
        if (playerComp.state == State.HORNATTACK) {
            HornAttackEvent.stop(player);
        }
        playerComp.state = State.RAINBOW;
        playerComp.stateTimer = GameConstants.RAINBOW_MODE_TIME;
        
    }
    
    @Override
    public void onHornAttackStart(Entity player) {
        PlayerComponent playerComp = ComponentMappers.player.get(player);
        playerComp.state = State.HORNATTACK;
        playerComp.stateTimer = GameConstants.HORN_MODE_TIME;
        
        
    }
    
    @Override
    public void onHornAttackStart() {
        if (getEntities().size()>0){
            logger.debug("Hornattack startet{}");
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.state=State.HORNATTACK;
            playerComp.stateTimer=GameConstants.HORN_MODE_TIME;
        }
    public void onHornAttackStop(Entity player) {
PlayerComponent playerComp = ComponentMappers.player.get(player);
playerComp.hornAttackCooldown=GameConstants.HORN_MODE_COOLDOWN;
        }
}
        
    public void onHornAttackStart() {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.state=State.HORNATTACK;
            playerComp.stateTimer=GameConstants.HORN_MODE_TIME;
        }
    }
    
    @Override
    public void onRainbowModeEnd(Entity player) {
            PlayerComponent playerComp = ComponentMappers.player.get(player);
            playerComp.hornAttackCooldown=GameConstants.HORN_MODE_COOLDOWN;
        }
    }

    @Override
    public void onSpuckChargeStart() {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.state=State.SPUCKCHARGE;
            playerComp.stateTimer=GameConstants.SPUCK_MODE_TIME;
        }
    }

    @Override
    public void onSpuckChargeStop() {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.spuckChargeCooldown=GameConstants.SPUCK_MODE_COOLDOWN;
        }
    }
    
}
