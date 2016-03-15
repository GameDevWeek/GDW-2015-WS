package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class KeyboardInputComponent extends Component implements Pool.Poolable {

    public boolean jump = false;
    public boolean spit = false;
    public boolean hornAttack = false;
    public boolean fly = false;
    
    public float direction = 0.0f;    

    @Override
    public void reset() {
        jump = spit = hornAttack = fly = false;
        direction = 0.0F;        
    }
}
