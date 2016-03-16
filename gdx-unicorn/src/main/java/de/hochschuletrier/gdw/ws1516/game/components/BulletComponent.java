package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * A bullet component.
 * @author Eileen
 * @version 1.0
 */
public class BulletComponent extends Component implements Pool.Poolable {
    
    /** Called when the player entity has been hit
     *  first entity is the bullet entity,
     *  second entity is the player entity that has been hit */
    public BiConsumer<Entity, Entity> onPlayerHit;
    
    /** Called when something, except the player, has been hit
     *  first entity is the bullet entity,
     *  second entity is the entity that has been hit */
    public BiConsumer<Entity, Entity> onEntityHit;
    
    /**
     * Called when something has been hit, including the player or other entity
     * the entity is the bullet entity that caused the hit
     */
    public Consumer<Entity> onHit;
    
    @Override
    public void reset() {
        onPlayerHit = null;
        onEntityHit = null;
        onHit = null;
    }
}