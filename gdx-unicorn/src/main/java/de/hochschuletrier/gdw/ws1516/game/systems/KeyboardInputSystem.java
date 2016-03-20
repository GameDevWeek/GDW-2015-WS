package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1516.events.BubblegumSpitSpawnEvent;
import de.hochschuletrier.gdw.ws1516.events.EndFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.events.HornCollisionEvent;
import de.hochschuletrier.gdw.ws1516.events.StartFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.LookDirection;

public class KeyboardInputSystem extends IteratingSystem implements InputProcessor,
                                                                    DeathEvent.Listener{
    private static final Logger logger        = LoggerFactory.getLogger(KeyboardInputSystem.class);
    private boolean             jump          = false;
    private boolean             spit          = false;
    private boolean             hornAttack    = false;
    private boolean             fly           = false;
    
    private boolean             moveRight=false;
    private boolean             moveLeft=false;
    private boolean             moveUp=false;
    private boolean             moveDown=false;
    
    private float               directionX, directionY = 0.0f;
    public LookDirection        lookDirection = MovementComponent.LookDirection.RIGHT;
    
    public KeyboardInputSystem(int priority) {
        super(Family.all(InputComponent.class, PlayerComponent.class).get(), priority);
    }
    
    // @Override
    // public public void addedToEngine(Engine engine) {
    // logger.debug("wurde Hinzugefügt{}");
    // };
    
    @Override
    public void addedToEngine (Engine engine) {
        super.addedToEngine(engine);
        DeathEvent.register(this);
    }

    @Override
    public void removedFromEngine (Engine engine) {
        super.removedFromEngine(engine);
        DeathEvent.unregister(this);
    }
    
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.SPACE:
                moveUp=true;
                jump = true;
                break;
            case Input.Keys.LEFT:
                moveLeft=true;
                break;
            case Input.Keys.RIGHT:
                moveRight=true;
                break;
            case Input.Keys.D:
                hornAttack = true;
                break;
            case Input.Keys.S:
                spit = true;
                break;
            case Input.Keys.F:
                fly = true;
                break;
            case Input.Keys.DOWN:
                moveDown=true;
                break;
        }
        return true;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.SPACE:
                moveUp=false;
                jump = false;
                break;
            case Input.Keys.LEFT:
                moveLeft=false;
                break;
            case Input.Keys.RIGHT:
                moveRight=false;
                break;
            case Input.Keys.F:
                fly = false;
                break;
            case Input.Keys.D:
                hornAttack = false;
                break;
            case Input.Keys.S:
                spit = false;
                break;
            case Input.Keys.DOWN:
                moveDown=false;
                break;
        }
        return true;
    }
    
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent input = entity.getComponent(InputComponent.class);
        PlayerComponent player=ComponentMappers.player.get(entity);
        MovementComponent movement=ComponentMappers.movement.get(entity);
        if (moveRight && moveLeft){
            directionX=0.0f;
        }else if (moveRight){
            lookDirection=LookDirection.RIGHT;
            directionX=+1.0f;
        }
        else if(moveLeft){
            lookDirection=LookDirection.LEFT;
            directionX=-1.0f;
        }else{
            directionX=0.0f;
        }
        if (moveUp && moveDown){
            directionY=0.0f;
        }else if (moveDown){
            directionY=+1.0f;
        }
        else if(moveUp){
            directionY=-1.0f;
        }else{
            directionY=0.0f;
        }
        input.directionX = directionX;
        input.directionY = directionY;
        input.startFly=fly;

        if (fly && player.state!=State.RAINBOW) {
            if (movement.state==MovementComponent.State.FLYING){
                EndFlyEvent.emit(entity);
            }else{
                if ( player.blueGumStacks > 0 ) {
                    StartFlyEvent.emit(entity, GameConstants.FLYING_TIME);
                    player.blueGumStacks--;
                }
            }
            fly = false;
        }


        if(hornAttack){
            if (movement.state==MovementComponent.State.FLYING){
                EndFlyEvent.emit(entity);
            }else{
                if (player.state!=State.RAINBOW && player.hornAttackCooldown<=0.0f){
                    HornAttackEvent.start(entity);
                }
            }
        }
        input.hornAttack = hornAttack;
        input.startJump = jump;
        
        // Spit button delta
        input.oldSpit = input.spit;
        input.spit = spit;
        
        if (movement.state==MovementComponent.State.FLYING) {
            input.spit = false;
            input.oldSpit = false;
            input.gumSpitCooldown = GameConstants.SPIT_COOLDOWN;
            input.gumSpitCharge = 0.0f;
        }
        
        // Spit cooldown
        input.gumSpitCooldown -= deltaTime;
        if (input.gumSpitCooldown <= 0){
            if (player.state==State.SPUCKCHARGE)
                player.state=State.NORMAL;
            input.gumSpitCooldown = 0;
        }
        
        //Charge spit
        if (input.spit){
            if(input.gumSpitCooldown == 0 && player.state!=State.RAINBOW) {
                input.gumSpitCharge += deltaTime;
                player.state=State.SPUCKCHARGE;
            }
        }
        //Emit spit
        if (input.gumSpitCooldown == 0 && player.state!=State.RAINBOW &&
            (input.oldSpit && !input.spit) || (input.gumSpitCharge > GameConstants.SPIT_CHARGE_TIME_TO_MAX)) {
            float force = (input.gumSpitCharge > GameConstants.SPIT_CHARGE_TIME_TO_MAX) ? 1.0f : input.gumSpitCharge / GameConstants.SPIT_CHARGE_TIME_TO_MAX;
            BubblegumSpitSpawnEvent.emit(force);
            input.gumSpitCooldown = GameConstants.SPIT_COOLDOWN;
            input.gumSpitCharge = 0.0f;
        }

        input.lookDirection = lookDirection;
        
    }

    @Override
    public void onDeathEvent(Entity entity) {
 
        //If the player dies, reset the spit system
        if (ComponentMappers.player.has(entity) && ComponentMappers.input.has(entity)) {
            
            //Fetch components
            InputComponent input = entity.getComponent(InputComponent.class);
            PlayerComponent player = entity.getComponent(PlayerComponent.class);
            
            //Reset spit system
            input.spit = false;
            input.oldSpit = false;
            input.gumSpitCharge = 0.0f;
            input.gumSpitCooldown = 0.0f;
            
        }
            
    }
    
}
