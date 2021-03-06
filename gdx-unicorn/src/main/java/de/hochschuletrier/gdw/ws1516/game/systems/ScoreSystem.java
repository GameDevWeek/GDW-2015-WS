package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Application;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.events.FinalScoreEvent;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent.GameStateType;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent.ScoreType;
/*
 * TODO listeners für Objekte einsammeln und zählen in diesem hoch
 */
public class ScoreSystem extends EntitySystem implements EntityListener , ScoreBoardEvent.Listener , GameOverEvent.Listener{

    private static final Logger logger = LoggerFactory.getLogger(ScoreSystem.class);
    private static long finalScore;
    private ScoreComponent scoreComponent;
    
    public ScoreSystem() {
        scoreComponent = null;
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        Family family=Family.all(ScoreComponent.class).get();
        engine.addEntityListener(family, this);
        ScoreBoardEvent.register(this);
        GameOverEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
        ScoreBoardEvent.unregister(this);
        GameOverEvent.unregister(this);
    }

    @Override
    public void update(float deltaTime) {
        {
            if(scoreComponent != null)
            {
                scoreComponent.playedSeconds += deltaTime;
                FinalScoreEvent.emit(getFinalScore(),scoreComponent);
            }
        }
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
        case BONBON:
            scoreComponent.bonbons += value;
                break;
        case BUBBLE_GUM:
            scoreComponent.bubblegums += value;
                break;
        case DEATH:
            scoreComponent.lives -= value;
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
        
        FinalScoreEvent.emit(getFinalScore(),scoreComponent);
    }

    public long getFinalScore()
    {
        long score = finalScore = GameConstants.SCORE_BASEPOINTS + scoreComponent.chocoCoins * GameConstants.SCORE_CHOCOCOINS_POINTS
                + scoreComponent.bonbons * GameConstants.SCORE_BONBONS_POINTS +
                (long)(GameConstants.SCORE_TIME_POINTS * scoreComponent.playedSeconds  ) +
                scoreComponent.lives  * GameConstants.SCORE_LIVES + 
                scoreComponent.killedEnemies * GameConstants.SCORE_KILLED_ENEMIES  +
                scoreComponent.killedObstacles * GameConstants.SCORE_KILLED_OBSTACLES +
                scoreComponent.hits * GameConstants.SCORE_HITS; 
        return (score>0)?score:0;
        
    }
   
    public static long getFinalScoreStatic() {
        return finalScore;
    }

    @Override
    public void onGameOverEvent(boolean won, String mapToLoad) {
        finalScore = getFinalScore();
        
    }

    
}
