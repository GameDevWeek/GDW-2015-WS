package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent.HitType;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;

public class SandBoxEventLogger implements DeathEvent.Listener , HitEvent.Listener , GameOverEvent.Listener  {

    private static final Logger logger = LoggerFactory.getLogger(SandBoxEventLogger.class);

    public SandBoxEventLogger() {
        HitEvent.register(this);
        DeathEvent.register(this);
        GameOverEvent.register(this);
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
    
    

}
