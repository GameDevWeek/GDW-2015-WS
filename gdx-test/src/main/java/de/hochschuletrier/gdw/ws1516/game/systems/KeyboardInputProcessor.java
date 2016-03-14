package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1516.events.PlayerHornAttackEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerJumpEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerMoveEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerSpitGumEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerStartFlyingEvent;

public class KeyboardInputProcessor implements InputProcessor {
    
    public static final int tasteA = Input.Keys.J;
    public static final int tasteB = Input.Keys.K;
    public static final int tasteC = Input.Keys.L;

    @Override
    public boolean keyDown(int keycode) { //constantly emits event when key down, bad. 
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.SPACE:
            case Input.Keys.W:
                PlayerJumpEvent.emit("Jump");
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                PlayerMoveEvent.emit("Left");
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                PlayerMoveEvent.emit("Right");
                break;
            case tasteA:
                PlayerHornAttackEvent.emit("Horn");
                break;
            case tasteB:
                PlayerSpitGumEvent.emit("Spit");
                break;
            case tasteC:
                PlayerStartFlyingEvent.emit("Fly");
        }
        return true;
    }
    

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
    

}
