package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InputComponent extends Component implements Pool.Poolable {

    public boolean jump = false;
    public boolean spit = false;
    public boolean hornAttack = false;
    public boolean fly = false;
    
    public float directionX = 0.0f;    
    public float directionY = 0.0f; 
    
    @Override
    public void reset() {
        jump = spit = hornAttack = fly = false;
        directionX=directionY = 0.0F;        
    }
}
