package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.LookDirection;

public class InputComponent extends Component implements Pool.Poolable {

    public boolean startJump = false;
    public boolean hornAttack = false;
    public boolean startFly = false;
    
    public float directionX = 0.0f;    
    public float directionY = 0.0f; 
    
    public LookDirection lookDirection = MovementComponent.LookDirection.RIGHT;
    
    public boolean spit = false;
    public boolean oldSpit = false;
    public float gumSpitCharge = 0.0f;
    public float gumSpitCooldown = 0.0f;
    
    @Override
    public void reset() {
        startJump = spit = hornAttack = startFly = false;
        directionX=directionY = 0.0F;       
        gumSpitCharge = 0.0f;
        gumSpitCooldown = 0.0f;
        lookDirection = MovementComponent.LookDirection.RIGHT;
    }
}

