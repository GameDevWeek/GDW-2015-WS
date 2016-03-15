package de.hochschuletrier.gdw.ws1516.game.systems.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;

public class KeyboardInputSystem extends IteratingSystem implements InputProcessor {

    private static class KeyState {
        private boolean left;
        private boolean right;
        private boolean up;
        private boolean down;
        private boolean shoot;
    }
    
    private final KeyState []states = {new KeyState(), new KeyState()};
    
    public KeyboardInputSystem() {
        super(Family.all(InputComponent.class).get());
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent input = ComponentMappers.input.get(entity);
        if(input.index > 1)
            return;
        KeyState state = states[input.index];
        
        float velX = 0, velY = 0;
        if (state.left) {
            velX -= 1;
        }
        if (state.right) {
            velX += 1;
        }
        if (state.up) {
            velY -= 1;
        }
        if (state.down) {
            velY += 1;
        }
        
        input.moveDirection.set(velX, velY).nor();
        if(!input.moveDirection.isZero())
            input.lastMoveDirection.set(input.moveDirection);
        
        if(state.shoot) {
            input.shoot = true;
            state.shoot = false;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keyUpDown(keycode, true);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!keyUpDown(keycode, false)) {
            if (keycode == Input.Keys.M)
                states[0].shoot = true;
            else if (keycode == Input.Keys.Q)
                states[1].shoot = true;
        }

        return true;
    }

    private boolean keyUpDown(int keycode, final boolean value) {
        switch (keycode) {
            case Input.Keys.LEFT:
                states[0].left = value;
                return true;
            case Input.Keys.RIGHT:
                states[0].right = value;
                return true;
            case Input.Keys.UP:
                states[0].up = value;
                return true;
            case Input.Keys.DOWN:
                states[0].down = value;
                return true;
            case Input.Keys.A:
                states[1].left = value;
                return true;
            case Input.Keys.D:
                states[1].right = value;
                return true;
            case Input.Keys.W:
                states[1].up = value;
                return true;
            case Input.Keys.S:
                states[1].down = value;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
    }
}
