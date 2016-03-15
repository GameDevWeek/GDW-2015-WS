package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1516.game.components.KeyboardInputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;

public class KeyboardInputSystem extends IteratingSystem implements InputProcessor {
    
    private boolean jump = false;
    private boolean spit = false;
    private boolean hornAttack = false;
    private boolean fly = false;
    
    private float direction = 0.0f;    

    
    public KeyboardInputSystem(Family family) {
        super(Family.all(KeyboardInputComponent.class, PlayerComponent.class).get());
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.SPACE:
            case Input.Keys.W:
                jump = true;
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                direction -= 1.0f;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                direction += 1.0f;
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
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.SPACE:
            case Input.Keys.W:
                jump = false;
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                direction += 1.0f;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                direction -= 1.0f;
                break;
            case Input.Keys.J:
                hornAttack = false;
                break;
            case Input.Keys.K:
                spit = false;
                break;
            case Input.Keys.L:
                fly = false;
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
        
        KeyboardInputComponent input = entity.getComponent(KeyboardInputComponent.class);
        input.direction = direction;
        input.fly = fly;
        input.hornAttack = hornAttack;
        input.jump = jump;
        input.spit = spit;
                
    }

    
    
}
