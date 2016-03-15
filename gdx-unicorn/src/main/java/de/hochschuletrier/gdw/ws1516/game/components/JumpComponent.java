package de.hochschuletrier.gdw.ws1516.game.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.ws1516.game.GameConstants;



public class JumpComponent extends Component implements Poolable {

    public boolean isGrounded=false;
    public float jumpImpulse=GameConstants.PLAYER_JUMP_IMPULSE;
    
    @Override
    public void reset()
    {
        isGrounded=false;
        jumpImpulse=GameConstants.PLAYER_JUMP_IMPULSE;
    }
    
}
