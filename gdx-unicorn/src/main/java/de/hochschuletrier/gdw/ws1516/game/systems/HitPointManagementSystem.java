package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent.HitType;
import de.hochschuletrier.gdw.ws1516.game.components.HitPointsComponent;
import de.hochschuletrier.gdw.ws1516.game.components.LiveComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRestartEvent;

public class HitPointManagementSystem extends EntitySystem implements HitEvent.Listener, DeathEvent.Listener {

    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<LiveComponent> lm = ComponentMapper.getFor(LiveComponent.class);
    private ComponentMapper<HitPointsComponent> hm = ComponentMapper.getFor(HitPointsComponent.class);
    private Engine engine;
    
    @Override
    public void addedToEngine(Engine engine) {
        HitEvent.register(this);
        DeathEvent.register(this);
        this.engine = engine;
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        HitEvent.unregister(this);
        DeathEvent.unregister(this);
        this.engine = null;
    }
    
    @Override
    public void onHitEvent(Entity entity, HitType type, int value) {
        PlayerComponent playerComp = pm.get(entity);
        HitPointsComponent hpComp = hm.get(entity);
        hpComp.value--;
        
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
            GameRestartEvent.emit();
        else
            GameOverEvent.emit();
    }
}
