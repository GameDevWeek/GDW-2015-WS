package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ParticleTestComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.RenderLayerComponent;

public class ParticleRenderSystem extends SubSystem {
    public ParticleRenderSystem() 
    {
        super(Family.all(PositionComponent.class, ParticleTestComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) 
    {        
        ParticleTestComponent component = ComponentMappers.particleTest.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        for(ParticleEmitter emitter : component.effect.getEmitters())
        {
            emitter.update(deltaTime);
        }
        component.effect.setPosition(position.x, position.y);
        component.effect.draw(DrawUtil.batch);
    }
}