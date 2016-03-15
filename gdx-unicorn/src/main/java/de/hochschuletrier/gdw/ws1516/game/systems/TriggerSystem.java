package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent.ScoreType;
import de.hochschuletrier.gdw.ws1516.events.TriggerEvent;
/*
 * TODO listeners für Objekte einsammeln und zählen in diesem hoch
 */
public class TriggerSystem extends EntitySystem implements TriggerEvent.Listener , EntityListener{

    private static final Logger logger = LoggerFactory.getLogger(TriggerSystem.class);
    
    private Entity unicorn = null;
    
    public TriggerSystem() {
        
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        Family family=Family.one(PlayerComponent.class).get();
        engine.addEntityListener(family, this);
        TriggerEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        // TODO Auto-generated method stub
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
        TriggerEvent.unregister(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        if (entity.getComponent(PlayerComponent.class)!=null){
            unicorn=entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (entity==unicorn){
            unicorn=null;
        }
    }
    @Override
    public void onTriggerEvent(TriggerEvent.Action type,Entity triggeringEntity) {
        switch( type)
        {
        case DEATH_ZONE:            
            
              DeathEvent.emit(triggeringEntity);
             
            break; 
        default:
            logger.warn("unhandeld TriggerEvent : {}",type.toString());
            break;
        }
        
    }

 
}
