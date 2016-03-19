package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;


public class MovementComponent extends Component implements Pool.Poolable{
    
    
    public float speed = GameConstants.PLAYER_SPEED;
    public float jumpImpulse=GameConstants.PLAYER_JUMP_IMPULSE;
    public float velocityX = 0;
    public float velocityY = 0;
    public float remainingStateTime=0;
    public LookDirection lookDirection = LookDirection.RIGHT;
    
    public boolean isOnPlatform;
    public PhysixBodyComponent onPlatformBody;
    
    public static enum State{
        ON_GROUND,
        FLYING,
        FALLING,
        JUMPING,
        LANDING,
        GLUED,
        SHOOTING
    }
    
    public static enum LookDirection {
        LEFT(-1.0f),
        RIGHT(1.0f);
        
        float cosine;
        
        LookDirection(float cosine) {
            this.cosine = cosine;
        }
        
        public float getCosine() {
            return this.cosine;
        }
        
    }
    
    public State state=State.ON_GROUND;
    
    @Override
    public void reset() {
        speed = GameConstants.PLAYER_SPEED;
        velocityX = velocityY=0;
        state = State.ON_GROUND;
        jumpImpulse=GameConstants.PLAYER_JUMP_IMPULSE;
        lookDirection = LookDirection.RIGHT;
        isOnPlatform = false;
    }
    
    
    
    
    
}
