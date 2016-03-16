package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ws1516.game.GameConstants;


public class MovementComponent extends Component implements Pool.Poolable{
    
    
    public float speed = GameConstants.PLAYER_SPEED;
    public float jumpImpulse=GameConstants.PLAYER_JUMP_IMPULSE;
    public float velocityX = 0;
    public float velocityY = 0;
    public static enum State{
        ON_GROUND,
        FLYING,
        FALLING,
        JUMPING
    }
    public State state=State.ON_GROUND;
    
    @Override
    public void reset() {
        speed = GameConstants.PLAYER_SPEED;
        velocityX = velocityY=0;
        state = State.ON_GROUND;
        jumpImpulse=GameConstants.PLAYER_JUMP_IMPULSE;
    }
    
    
    
    
    
}
