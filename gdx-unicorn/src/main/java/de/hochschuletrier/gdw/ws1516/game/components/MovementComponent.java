package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ws1516.game.GameConstants;

public class MovementComponent extends Component implements Pool.Poolable{
    
    
    public float speed = GameConstants.PLAYER_SPEED;
    public float velocityX = 0;
    
    
    @Override
    public void reset() {
        // TODO Auto-generated method stub
        speed = GameConstants.PLAYER_SPEED;
        velocityX = 0;
    }
    
    
    
    
    
}
