package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Application;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.AttackPatternComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.events.PauseGameEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent.ScoreType;
/*
 * TODO listeners für Objekte einsammeln und zählen in diesem hoch
 */
public class EnemyHandlingSystem extends IteratingSystem  {

    
    public class Action {
        
    }

    public static enum EnemyType {
        HUNTER,PAPARAZZI
    }
    public static enum Behaviour {
        STANDING,FOLLOW_PATH,ATTACK_UNICORN
    }


    private Entity unicorn;

    
    public EnemyHandlingSystem() {
        super(Family.all(EnemyBehaviourComponent.class,EnemyTypeComponent.class).get());
    }
    


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyBehaviourComponent behaviour = ComponentMappers.enemyBehaviour.get(entity);
        EnemyTypeComponent type = ComponentMappers.enemyType.get(entity);
        AttackPatternComponent pattern = ComponentMappers.enemyPattern.get(entity);
       
        
        if( behaviour.canSeeUnicorn )
        {
            behaviour.behaviourState = Behaviour.ATTACK_UNICORN; 
            behaviour.behaviourState = (behaviour.canSeeUnicorn)?Behaviour.ATTACK_UNICORN:Behaviour.FOLLOW_PATH;
            // TODO pattern beachten
            getNextEnemyStep(entity);
        }
        
    }



    private void getNextEnemyStep(Entity entity) {
        // TODO Auto-generated method stub
        
    }
    
}
