package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1516.game.components.KeyboardInputComponent;

public class KeyboardInputSystem extends IteratingSystem implements InputProcessor {
    
    private boolean isListener = false;
    
    private boolean jump = false;
    private boolean spit = false;
    private boolean hornAttack = false;
    private boolean fly = false;
    
    private float direction = 0.0f;    


    public KeyboardInputSystem(Family family, int priority) {
        super(family, priority);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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
        return false;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (true) { //check if given entity is a player entity, placeholder
            KeyboardInputComponent input = entity.getComponent(KeyboardInputComponent.class);
            
            input.direction = direction;
            input.fly = fly;
            input.hornAttack = hornAttack;
            input.jump = jump;
            input.spit = spit;
            
        }
        
    }

    
    
}
