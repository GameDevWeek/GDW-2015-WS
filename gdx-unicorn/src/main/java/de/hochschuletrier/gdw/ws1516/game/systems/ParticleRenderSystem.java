package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.ParticleComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class ParticleRenderSystem extends SortedSubIteratingSystem.SubSystem 
{
    public ParticleRenderSystem() 
    {
        super(Family.all(PositionComponent.class, ParticleComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) 
    {
        ParticleComponent component = ComponentMappers.particleTest.get(entity);
        
        PositionComponent position = ComponentMappers.position.get(entity);
        
        for(ParticleEmitter emitter : component.effect.getEmitters())
        {
            emitter.start();
            emitter.setPosition(position.x, position.y);
            emitter.draw(DrawUtil.batch, deltaTime);
        }
        component.effect.setPosition(position.x, position.y);
        component.effect.draw(DrawUtil.batch);
    }
}