package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.enemyStates.DoNothingState;
import de.hochschuletrier.gdw.ws1516.game.systems.enemyStates.EnemyBaseState;

/**
 * Beschreibt das derzeitige verhalten des enemies
 * @author Tobi
 *
 */
public class EnemyBehaviourComponent extends Component implements Poolable{

    public EnemyBaseState currentState;
    public boolean canSeeUnicorn;
    public int pathIndex;
    public float lastJumped;
    
    
    @Override
    public void reset() {
        currentState=null;
        
    }

}
