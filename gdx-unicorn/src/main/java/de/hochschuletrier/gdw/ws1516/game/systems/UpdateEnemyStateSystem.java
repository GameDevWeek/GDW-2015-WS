package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.events.EnemyStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent.Behaviour;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;

public class UpdateEnemyStateSystem extends IteratingSystem {

    private ComponentMapper<EnemyBehaviourComponent> em = ComponentMapper.getFor(EnemyBehaviourComponent.class);
    
    public UpdateEnemyStateSystem() {
        super(Family.all(EnemyTypeComponent.class, EnemyBehaviourComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyBehaviourComponent behaviour = em.get(entity);
        
        if (behaviour.canSeeUnicorn) {
            if (behaviour.behaviourState != Behaviour.ATTACK) {
                EnemyStateChangeEvent.emit(entity, behaviour.behaviourState, Behaviour.ATTACK);
                behaviour.behaviourState = Behaviour.ATTACK;
            }
        } else {
            if (behaviour.behaviourState != Behaviour.FOLLOW_PATH) {
                EnemyStateChangeEvent.emit(entity, behaviour.behaviourState, Behaviour.FOLLOW_PATH);
                behaviour.behaviourState = Behaviour.FOLLOW_PATH;
            }
        }
    }

}
