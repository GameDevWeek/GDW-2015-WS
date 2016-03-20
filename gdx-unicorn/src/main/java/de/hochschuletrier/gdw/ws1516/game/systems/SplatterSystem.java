package de.hochschuletrier.gdw.ws1516.game.systems;

import java.awt.image.ComponentSampleModel;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent.Listener;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.ForegroundParticleComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;

public class SplatterSystem extends IteratingSystem implements Listener {

    public SplatterSystem(int priority) {
        super(Family.all().get(), priority);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        DeathEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        DeathEvent.unregister(this);
    }

    @Override
    public void onDeathEvent(Entity entity) {
        PositionComponent deathPos = ComponentMappers.position.get(entity);
        if(ComponentMappers.enemyType.has(entity) && deathPos != null)
        {
            Entity splatter = EntityCreator.createEntity("blood_splatter", deathPos.x, deathPos.y);
            PositionComponent splatterPos = ComponentMappers.position.get(splatter);
            if(splatterPos != null)
            {
                splatterPos.x = deathPos.x;
                splatterPos.y = deathPos.y;
            }
            ForegroundParticleComponent particleComponent = ComponentMappers.foregroundParticle.get(entity);
            if(particleComponent != null)
            {
                particleComponent.effect.start();
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Do nothing
    }

}
