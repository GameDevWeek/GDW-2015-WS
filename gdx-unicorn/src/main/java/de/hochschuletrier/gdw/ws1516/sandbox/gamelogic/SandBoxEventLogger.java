package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent.HitType;
import de.hochschuletrier.gdw.ws1516.events.TriggerEvent;
import de.hochschuletrier.gdw.ws1516.events.TriggerEvent.Action;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent;

public class SandBoxEventLogger implements DeathEvent.Listener , HitEvent.Listener , GameOverEvent.Listener, TriggerEvent.Listener, GameRespawnEvent.Listener  {

    private static final Logger logger = LoggerFactory.getLogger(SandBoxEventLogger.class);

    public SandBoxEventLogger() {
        HitEvent.register(this);
        DeathEvent.register(this);
        GameOverEvent.register(this);
        TriggerEvent.register(this);
        GameRespawnEvent.register(this);
    }
    
    @Override
    public void onDeathEvent(Entity entity) {
        
        logger.info("DeathEvent thrown");
    }

    @Override
    public void onHitEvent(Entity entity, HitType type, int value) {

        logger.info("HitEvent thrown");
        
    }

    @Override
    public void onGameOverEvent() {
        logger.info("Game is Over - event");
        
    }

    @Override
    public void onTriggerEvent(Action action, Entity triggeringEntity) {
        logger.info("Trigger Event {}",action.toString());
        
    }

    @Override
    public void onGameRepawnEvent() {
        logger.info("Game Respawn Event thrown");
        
    }
    
    

}
