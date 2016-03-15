package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ParticleTestComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.RenderLayerComponent;

public class ParticleRenderSystem extends SortedSubIteratingSystem.SubSystem 
{
    public ParticleRenderSystem() 
    {
        super(Family.all(PositionComponent.class, ParticleTestComponent.class).get());
        System.out.println("ParticleRenderSystem");
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) 
    {        
//        System.out.println("ParticleRenderSystem Process");
        SpriteBatch batch = new SpriteBatch();
        
        ParticleTestComponent component = ComponentMappers.particleTest.get(entity);
        if(component != null)
        {
            System.out.println("o Render because not null");
        }
        else
        {
            System.out.println("x Dont render because null");
        }
        PositionComponent position = ComponentMappers.position.get(entity);
        
        
        for(ParticleEmitter emitter : component.effect.getEmitters())
        {
            batch.begin();
            emitter.start();
            emitter.update(deltaTime);
            emitter.setPosition(position.x, position.y);
            emitter.draw(batch, deltaTime);
        }
        component.effect.setPosition(position.x, position.y);
        component.effect.draw(DrawUtil.batch);
        
//        System.out.println("ParticleRenderSystem processEnde");
    }
}