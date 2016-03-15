package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent.ScoreType;
/*
 * TODO listeners für Objekte einsammeln und zählen in diesem hoch
 */
public class ScoreSystem extends EntitySystem implements EntityListener , ScoreBoardEvent.Listener{

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
        ScoreComponent component=ComponentMappers.score.get(entity);
        if (component!=null){
            scoreComponent = component;
        }
    }
    @Override
    public void entityRemoved(Entity entity) {
        ScoreComponent component=ComponentMappers.score.get(entity);
        if (component.equals(scoreComponent)){
            scoreComponent = null;
        }
    }

    @Override
    public void onScoreEvent(ScoreType type, int value) {
        switch( type)
        {
        case CHOCO_COIN:
            scoreComponent.chocoCoins += value;
                break;
        case BUBBLE_GUM:
            scoreComponent.bubblegums += value;
                break;
        case DEATH:
            scoreComponent.deaths += value;
                break;
        case HIT:
            scoreComponent.hits += value;
                break;
        case KILLED_ENEMIE:
            scoreComponent.killedEnemies += value;
                break;
        case KILLED_OBSTACLE:
            scoreComponent.killedObstacles += value;
                break;
        }
        
    }

}
