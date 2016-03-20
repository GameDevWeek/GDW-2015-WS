package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.UnicornIdleAnimationEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;


public class DeathAnimationSystem extends IteratingSystem implements DeathEvent.Listener 
{
    UnicornIdleAnimationEvent.Listener l;
    public DeathAnimationSystem(int priority) 
    {
        super(Family.all().get(), priority);
    }
    
    @Override
    public void addedToEngine(Engine engine) 
    {
        super.addedToEngine(engine);
        DeathEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) 
    {
        super.removedFromEngine(engine);
        DeathEvent.unregister(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) 
    {

    }

    @Override
    public void onDeathEvent(Entity entity) 
    {
        PositionComponent deathPos = ComponentMappers.position.get(entity);
        
        if(ComponentMappers.enemyType.has(entity) && deathPos != null)
        {
            Entity deathDummy = EntityCreator.createEntity("hunterDeathDummy", deathPos.x, deathPos.y);
            PositionComponent dummyPos = ComponentMappers.position.get(deathDummy);
            if(dummyPos != null)
            {
                dummyPos.x = deathPos.x;
                dummyPos.y = deathPos.y;
            }
        }   
    }

}
