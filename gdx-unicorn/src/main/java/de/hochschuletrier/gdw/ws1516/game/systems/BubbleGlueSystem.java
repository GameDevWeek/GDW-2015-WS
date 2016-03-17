package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.components.BubbleGlueComponent;

public class BubbleGlueSystem extends EntitySystem {
    private static final Logger logger = LoggerFactory.getLogger(BubbleGlueSystem.class);
    
    private PooledEngine engine;
    
    public BubbleGlueSystem(PooledEngine engine) {
        this.engine = engine;
    }
    
    @Override
    public void update(float deltaTime) {
        
        ImmutableArray<Entity> entitys = engine.getEntitiesFor(Family.all(BubbleGlueComponent.class).get());
        for (Entity entity : entitys) {
              //Fetch components
                BubbleGlueComponent glueComponent = ComponentMapper.getFor(BubbleGlueComponent.class).get(entity);
                PhysixBodyComponent body = ComponentMapper.getFor(PhysixBodyComponent.class).get(glueComponent.gluedEntity);
                
                if (body == null) {
                    engine.removeEntity(entity);
                    continue;
                }
                
                //Glue (evtl modify component)
                body.setX(glueComponent.gluedToPosition.x);
                body.setY(glueComponent.gluedToPosition.y);
                
                //Arresto momentum
                body.setLinearVelocity(0, 0);
                
                //Cooldown
                glueComponent.timeRemaining -= deltaTime;
                    
                if (glueComponent.timeRemaining <= 0) {
                    engine.removeEntity(entity);
                }
        }
            
    }
}