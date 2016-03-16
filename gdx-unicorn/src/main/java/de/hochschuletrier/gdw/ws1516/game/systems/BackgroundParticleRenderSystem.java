package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.BackgroundParticleComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ParticleTestComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class BackgroundParticleRenderSystem extends SortedSubIteratingSystem.SubSystem 
{
    public BackgroundParticleRenderSystem() 
    {
        super(Family.all(PositionComponent.class, BackgroundParticleComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) 
    {
        BackgroundParticleComponent component = ComponentMappers.bgParticle.get(entity);
        
        PositionComponent position = ComponentMappers.position.get(entity);
        
        for(ParticleEmitter emitter : component.bgEffect.getEmitters())
        {
            emitter.start();
            emitter.setPosition(position.x, position.y);
            emitter.draw(DrawUtil.batch, deltaTime);
        }
        component.bgEffect.setPosition(position.x, position.y);
        component.bgEffect.draw(DrawUtil.batch);
    }

}
