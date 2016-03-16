package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem;

public class EnemyTypeComponent extends Component implements Poolable{
    
    public static enum EnemyType {
        HUNTER,
        PAPARAZZI;
    }
    
    public EnemyType type;
    
    @Override
    public void reset() {
        
    }

}
