package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;

public class DummyEnemyExecutionSystem extends EntitySystem implements EnemyActionEvent.Listener {

    private static final Logger logger = LoggerFactory.getLogger(SandBoxEventLogger.class);
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        EnemyActionEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        EnemyActionEvent.unregister(this);
        
    }
    
    @Override
    public void onEnemyActionEvent(Entity enemy,Type action, float strength) 
    {

        PhysixBodyComponent body = ComponentMappers.physixBody.get(enemy);
        
        switch(action)
        {
        case MOVE:
            body.setLinearVelocityX(strength);
        break;
        case JUMP:
            body.applyImpulse(0,strength);
        break;
        case SHOOT:
                /// Fire Bullet
        break;
        default:
            break;
        }
    }


}
