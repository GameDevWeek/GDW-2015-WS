package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem;

public class AttackPatternComponent extends Component implements Poolable{

    public ArrayList<EnemyHandlingSystem.Action> pattern = new ArrayList<EnemyHandlingSystem.Action>();
    
    @Override
    public void reset() {
        
    }

}
