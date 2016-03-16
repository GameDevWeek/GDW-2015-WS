package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1516.events.EndFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.StartFlyEvent;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.LookDirection;

public class KeyboardInputSystem extends IteratingSystem implements InputProcessor {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardInputSystem.class);
    private boolean jump = false;
    private boolean spit = false;
    private boolean hornAttack = false;
    private boolean fly = false;
    
    //for testing
    private boolean stopflying=false;
    
    private float directionX,directionY  = 0.0f;    
    public LookDirection lookDirection = MovementComponent.LookDirection.RIGHT;
    
    public KeyboardInputSystem(int priority) {
        super(Family.all(InputComponent.class, PlayerComponent.class).get(),priority);
    }

//    @Override
//    public public void addedToEngine(Engine engine) {
//        logger.debug("wurde Hinzugefügt{}");
//    };
    
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.SPACE:
            case Input.Keys.W:
                directionY -= 1.0f;
                jump = true;
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                directionX -= 1.0f;
                lookDirection = MovementComponent.LookDirection.LEFT;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                directionX += 1.0f;
                lookDirection = MovementComponent.LookDirection.RIGHT;
                break;
            case Input.Keys.J:
                hornAttack = true;
                break;
            case Input.Keys.K:
                spit = true;
                break;
            case Input.Keys.L:
                fly = true;
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                directionY +=1.0f;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.SPACE:
            case Input.Keys.W:
                directionY +=1.0f;
                jump = false;
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                directionX += 1.0f;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                directionX -= 1.0f;
                break;
                //for testing reasons
            case Input.Keys.NUM_1:
                fly=true;
//                hornAttack = false;
                break;
                //for testing reasons
            case Input.Keys.NUM_2:
                stopflying=true;
//                hornAttack = false;
                break;
            case Input.Keys.K:
                spit = false;
                break;
            case Input.Keys.L:
                fly = false;
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                directionY -=1.0f;
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
        input.directionX = directionX;
        input.directionY=directionY;
        input.startFly = fly;
        if(fly){
            StartFlyEvent.emit(entity,10);
            fly=false;
        }
//        if(stopflying){
//            EndFlyEvent.emit(entity);
//            fly=false;
//            stopflying=false;
//        }
        input.hornAttack = hornAttack;
        input.startJump = jump;
        input.spit = spit;
         
        input.lookDirection = lookDirection;
        
    }

    
    
}
