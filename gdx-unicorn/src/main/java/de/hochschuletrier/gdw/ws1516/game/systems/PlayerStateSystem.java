package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.EndFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent.ScoreType;
import de.hochschuletrier.gdw.ws1516.events.StartFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.ThrowBackEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import javafx.scene.input.ScrollEvent;

public class PlayerStateSystem extends IteratingSystem implements RainbowEvent.Listener,
                                                                  HornAttackEvent.Listener,
                                                                  StartFlyEvent.Listener,
                                                                  EndFlyEvent.Listener,
                                                                  ThrowBackEvent.Listener,
                                                                  DeathEvent.Listener,
                                                                  HitEvent.Listener {
    
    private static final Logger logger = LoggerFactory.getLogger(PlayerStateSystem.class);
    private float maxGameBottom;
    private float maxGameRight;
    private int maxGameLeft;
    private int maxGameTop;
    
    public PlayerStateSystem() 
    {
        super(Family.all(PlayerComponent.class,MovementComponent.class).get());
    }
    
    @Override
    public void addedToEngine(Engine engine)
    {
        super.addedToEngine(engine);
        RainbowEvent.register(this);
        HornAttackEvent.register(this);
        StartFlyEvent.register(this);
        EndFlyEvent.register(this);
        ThrowBackEvent.register(this);
        DeathEvent.register(this);
        HitEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) 
    {
        super.removedFromEngine(engine);
        RainbowEvent.unregister(this);
        HornAttackEvent.unregister(this);
        StartFlyEvent.unregister(this);
        EndFlyEvent.unregister(this);
        ThrowBackEvent.unregister(this);
        DeathEvent.unregister(this);
        HitEvent.unregister(this);
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) 
    {
        boolean dieLater = false;
        MovementComponent movementComp = ComponentMappers.movement.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        playerComp.stateTimer = Math.max(playerComp.stateTimer - deltaTime, 0);
        
        playerComp.hornAttackCooldown = Math.max(playerComp.hornAttackCooldown - deltaTime, 0);
        playerComp.throwBackCooldown = Math.max(playerComp.throwBackCooldown - deltaTime, 0);
        playerComp.invulnerableTimer = Math.max(playerComp.invulnerableTimer - deltaTime, 0);
        if (playerComp.stateTimer <= 0.0f) {
            if (playerComp.state == State.HORNATTACK) 
            {
                HornAttackEvent.stop(entity);
            }
            if (playerComp.state == State.THROWBACK) {
                ThrowBackEvent.stop(entity);
            }
            if (playerComp.state==State.RAINBOW)
            {
                RainbowEvent.end(entity);
                movementComp.speed=GameConstants.PLAYER_SPEED;
                if ( playerComp.deathZoneCounter > 0 )
                {   /// kann hier nicht sterben, da RainbowMode , also dieLater
                    dieLater = true;
                }
            } 
            playerComp.state = State.NORMAL;
            if (dieLater)
            {
                DeathEvent.emit(entity);                
            }
        }
        if ( position.x < maxGameLeft || position.x > maxGameRight ||
                position.y < maxGameTop || position.y > maxGameBottom )
        {
            DeathEvent.emit(entity);
        }
    }
    
    @Override
    public void onRainbowCollect(Entity player) 
    {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            MovementComponent movementComp = ComponentMappers.movement.get(getEntities().get(0));
            if (playerComp.state==State.HORNATTACK)
            {
                HornAttackEvent.stop(player);
            }
            EndFlyEvent.emit(player);
            playerComp.state=State.RAINBOW;
            playerComp.stateTimer=GameConstants.RAINBOW_MODE_TIME;
            playerComp.deathZoneCounter = 0;
            movementComp.reset();
            movementComp.speed=GameConstants.PLAYER_SPEED*GameConstants.RAINBOW_SPEED_MODIFIER; 
            
        }
        
    }
    
    @Override
    public void onHornAttackStart(Entity player)
    {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.state = State.HORNATTACK;
            playerComp.stateTimer = GameConstants.HORN_MODE_TIME;
            playerComp.hornAttackCooldown = GameConstants.HORN_MODE_TIME;            
        }
    }
    
     
    public void onHornAttackStop(Entity player) 
    {

        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.hornAttackCooldown = GameConstants.HORN_MODE_COOLDOWN;
        }
    }

    @Override
    public void onStartFlyEvent(Entity entity, float time) 
    {

        if (getEntities().size()>0){
            MovementComponent move = ComponentMappers.movement.get(getEntities().get(0));
            move.speed = (float) (GameConstants.PLAYER_SPEED*0.75f);
        }
        
    }
    

    @Override
    public void onEndFlyEvent(Entity entity) 
    {
        if (getEntities().size()>0)
        {
            MovementComponent move = ComponentMappers.movement.get(getEntities().get(0));
            move.speed = (float) (GameConstants.PLAYER_SPEED);
        }     
    }

    @Override
    public void onThrowBackEventStart(Entity unicorn, float dirX) {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(unicorn);
            
            if (playerComp.state != State.NORMAL) {
                return;
            }
            
            playerComp.state = State.THROWBACK;
            playerComp.stateTimer = GameConstants.THROWBACK_MODE_COOLDOWN;
            playerComp.throwBackCooldown = GameConstants.THROWBACK_MODE_COOLDOWN;         
        }
    }

    @Override
    public void onThrowBackEventStop(Entity unicorn) {
        
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(unicorn);
            playerComp.throwBackCooldown = 0;
            playerComp.state = State.NORMAL;
        }
    }

    @Override
    public void onRainbowModeEnd(Entity player) {
        ComponentMappers.unicornAnimation.get(player).isInBlueMode = false;
        ComponentMappers.unicornAnimation.get(player).isInRainbowMode = false;
        EndFlyEvent.emit(player);
    }

    public void initializeDeathBorders(TiledMap map) {
        maxGameBottom = map.getHeight()*GameConstants.TILESIZE_X;
        maxGameRight = map.getWidth() * GameConstants.TILESIZE_Y;
        maxGameLeft = 0;
        maxGameTop = 0;
        
    }

    @Override
    public void onDeathEvent(Entity entity) {
        
        //If the player dies, end flying
        if (ComponentMappers.player.has(entity)) {
            EndFlyEvent.emit(entity);
            ScoreBoardEvent.emit(ScoreType.DEATH, 1);
        }
        
    }

    @Override
    public void onHitEvent(Entity wasHit, Entity source, int value) {
        //If the player dies, end flying
        if (ComponentMappers.player.has(wasHit)) {
            EndFlyEvent.emit(wasHit);
        }
    }
    
}
