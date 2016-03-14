package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;

/*
 * TODO listeners für Objekte einsammeln und zählen in diesem hoch
 */
public class ScoreSystem extends IteratingSystem{

    private ScoreComponent scoreComponent;
    
    public ScoreSystem() {
        super(Family.all(ScoreComponent.class).get());
        scoreComponent = null;
    }
    @Override
    public void update(float deltaTime) {
        getEntities().forEach((t)->{
            ScoreComponent component=t.getComponent(ScoreComponent.class);
            if (component !=null){
                scoreComponent=component;
            }
        });
        super.update(deltaTime);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        
    }

}
