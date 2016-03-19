package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Blocking gum component. When the unicorn spits on the ground, 
 * the gum blocks the way. This is accomplished by creating a "blockingGum" entity.
 * @author Seze
 * @version 1.0
 */
public class BlockingGumComponent extends Component implements Poolable {

    /** Time to despawn in seconds*/
    public float timeToDespawn;
    /** Callback is called when the blocking gum despawn's, entity is the blocking gum entity */
    public Consumer<Entity> onDespawn;
    
    @Override
    public void reset() {
        
    }

}