package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.BackgroundParticleComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ParticleComponent;
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
        MovementComponent movement = ComponentMappers.movement.get(entity);
        
        PositionComponent position = ComponentMappers.position.get(entity);
        
        boolean flip = ((movement.lookDirection) == (MovementComponent.LookDirection.LEFT));
        
//        System.out.println(flip);
        
        
        for(ParticleEmitter emitter : component.effect.getEmitters())
        {
            emitter.start();
            emitter.setPosition(position.x, position.y);
            if(flip)
            {
                //flip angles
            }
            emitter.draw(DrawUtil.batch, deltaTime);
        }
        
        component.effect.setPosition(position.x, position.y);
        component.effect.draw(DrawUtil.batch);
    }

}

//angleValue.setHigh(-angleValue.getHighMin(), -angleValue.getHighMax());
//angleValue.setLow(-angleValue.getLowMin(), -angleValue.getLowMax());
//
//gravityValue.setHigh(-gravityValue.getHighMin(), -gravityValue.getHighMax());
//gravityValue.setLow(-gravityValue.getLowMin(), -gravityValue.getLowMax());
//
//windValue.setHigh(-windValue.getHighMin(), -windValue.getHighMax());
//windValue.setLow(-windValue.getLowMin(), -windValue.getLowMax());
//
//rotationValue.setHigh(-rotationValue.getHighMin(), -rotationValue.getHighMax());
//rotationValue.setLow(-rotationValue.getLowMin(), -rotationValue.getLowMax());
//
//yOffsetValue.setLow(-yOffsetValue.getLowMin(), -yOffsetValue.getLowMax());
