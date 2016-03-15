package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent.HitType;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.HitPointsComponent;
import de.hochschuletrier.gdw.ws1516.game.components.LiveComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent;
import de.hochschuletrier.gdw.ws1516.events.HealEvent;
import de.hochschuletrier.gdw.ws1516.events.HealEvent.Listener;

public class HitPointManagementSystem extends EntitySystem implements HitEvent.Listener, DeathEvent.Listener, HealEvent.Listener{

    private static final Logger logger = LoggerFactory.getLogger(GameLogicTest.class);
    
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<LiveComponent> lm = ComponentMapper.getFor(LiveComponent.class);
    private ComponentMapper<HitPointsComponent> hm = ComponentMapper.getFor(HitPointsComponent.class);
    private Engine engine;
    
    @Override
    public void addedToEngine(Engine engine) {
        HitEvent.register(this);
        DeathEvent.register(this);
        HealEvent.register(this);
        this.engine = engine;
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        HitEvent.unregister(this);
        DeathEvent.unregister(this);
        HealEvent.unregister(this);
        this.engine = null;
    }
    
    @Override
    public void onHitEvent(Entity entity, HitType type, int value) {
        if (value > 0 )
        {
            PlayerComponent playerComp = pm.get(entity);
            HitPointsComponent hpComp = hm.get(entity);
            hpComp.value-= value;
            
            if (hpComp.value <= 0)
                DeathEvent.emit(entity);
            
            if (playerComp == null) //wenn es sich um einen Gegner handelt wars das soweit.
                return;
            
            //ansonsten war es das Einhorn => unterschiedliches Verhalten, je nach Art des HitPoints.
            switch (type) {
            case TOUCH:
                //TODO Einhorn ThrowBack? Über ThrowBackEvent?
                break;
            default:
                break;
            }
        } else
        {

            logger.warn("cannot damage for {} Hitpoints",value);
        }
    }

    @Override
    public void onDeathEvent(Entity entity) {
        PlayerComponent playerComp = pm.get(entity);
        
        if (playerComp == null) { //es handelt sich also um einen Gegner und nicht um das Einhorn, also Gegner entfernen.
            engine.removeEntity(entity);
            return;
        }
        
        //es handelt sich um das Einhorn, also Leben abziehen.
        LiveComponent liveComp = lm.get(entity);
        liveComp.value--;
        
        if (liveComp.value > 0)
            GameRespawnEvent.emit();
        else
            GameOverEvent.emit();
    }


    @Override
    public void onHealEvent(Entity entity, int value) {
        if (value > 0 )
        {
            HitPointsComponent hp = ComponentMappers.hp.get(entity);
            if ( hp != null ) 
            {
                hp.value = Math.min(hp.value+value,hp.max);
            }
        }else
        {
            logger.warn("cannot heal for {} Hitpoints",value);
        }
    }
}