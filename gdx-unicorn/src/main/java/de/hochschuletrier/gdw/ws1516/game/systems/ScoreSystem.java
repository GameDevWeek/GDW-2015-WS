package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;

/*
 * TODO listeners für Objekte einsammeln und zählen in diesem hoch
 */
public class ScoreSystem extends EntitySystem implements EntityListener{

    private ScoreComponent scoreComponent;
    
    public ScoreSystem() {
        scoreComponent = null;
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        Family family=Family.all(ScoreComponent.class).get();
        engine.addEntityListener(family, this);
    }

    @Override
    public void update(float deltaTime) {
        
    }
    @Override
    public void entityAdded(Entity entity) {
        ScoreComponent component=entity.getComponent(ScoreComponent.class);
        if (component!=null){
            scoreComponent = component;
        }
    }
    @Override
    public void entityRemoved(Entity entity) {
        ScoreComponent component=entity.getComponent(ScoreComponent.class);
        if (component.equals(scoreComponent)){
            scoreComponent = null;
        }
    }

}
