package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent.HitType;

public class SandBoxEventLogger implements DeathEvent.Listener , HitEvent.Listener {

    private static final Logger logger = LoggerFactory.getLogger(SandBoxEventLogger.class);

    public SandBoxEventLogger() {
        HitEvent.register(this);
        DeathEvent.register(this);
    }
    
    @Override
    public void onDeathEvent(Entity entity) {
        
        logger.info("DeathEvent thrown");
    }

    @Override
    public void onHitEvent(Entity entity, HitType type, int value) {

        logger.info("HitEvent thrown");
        
    }
    
    

}
