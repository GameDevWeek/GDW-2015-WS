package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent.EnemyType;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;


public class DeathAnimationSystem extends IteratingSystem implements DeathEvent.Listener 
{

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
        EnemyTypeComponent enemyType = ComponentMappers.enemyType.get(entity);
        
        if((enemyType != null) && (enemyType.type == EnemyType.HUNTER))
        {            
            Entity deathDummy = EntityCreator.createEntity("hunterDeathDummy", deathPos.x, deathPos.y);
            PositionComponent dummyPos = ComponentMappers.position.get(deathDummy);
            if(dummyPos != null)
            {
                dummyPos.x = deathPos.x;
                dummyPos.y = deathPos.y;
            }
            AnimationComponent animComp = ComponentMappers.animation.get(deathDummy);
            animComp.killWhenFinished = true;
        }   
    }

}
