package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.HashMap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1516.game.components.NameComponent;

public class NameSystem extends EntitySystem implements EntityListener {

    private HashMap<String, Entity> entities = new HashMap<>();

    public NameSystem() {
        super(0);
    }

    public NameSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(NameComponent.class).get();
        engine.addEntityListener(family, this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        entities.put(entity.getComponent(NameComponent.class).name, entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        entities.remove(entity.getComponent(NameComponent.class).name);
    }

    /** public accessor function to get an entity by name */
    public Entity getEntityByName(String name) {
        return entities.get(name);
    }

}
