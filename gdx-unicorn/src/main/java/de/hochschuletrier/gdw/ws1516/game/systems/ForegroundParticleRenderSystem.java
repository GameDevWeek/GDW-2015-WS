package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.RangedNumericValue;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.ForegroundParticleComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.LookDirection;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class ForegroundParticleRenderSystem extends SortedSubIteratingSystem.SubSystem
{
    public ForegroundParticleRenderSystem()
    {
        super(Family.all(PositionComponent.class, ForegroundParticleComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        ForegroundParticleComponent particleComponent = ComponentMappers.foregroundParticle.get(entity);
        MovementComponent movementComponent = ComponentMappers.movement.get(entity);
        PositionComponent positionComponent = ComponentMappers.position.get(entity);

        boolean isDestroyed = destroyIfFinished(entity, particleComponent);
        if(isDestroyed)
        {
            return;
        }
        
        if (movementComponent != null) {
            boolean flip = ((movementComponent.lookDirection) == (MovementComponent.LookDirection.LEFT));
            if (flip != particleComponent.isFlippedHorizontal)
                flipHorizontal(particleComponent);
        }

        boolean isMoving = movementComponent != null && Math.abs(movementComponent.velocityX) > 0.01f;
        
        for(int i = 0; i < particleComponent.effect.getEmitters().size; ++i)
        {
            ParticleEmitter emitter = particleComponent.effect.getEmitters().get(i);
            if(movementComponent != null && particleComponent.reduceEmissionIfIdle)
            {
                reduceEmissionIfIdle(particleComponent, emitter, isMoving, i);
            }
            float xOffset = 0f;
            if(isMoving)
            {
                xOffset = movementComponent.lookDirection == LookDirection.RIGHT ? particleComponent.offsetWhenMoving : -particleComponent.offsetWhenMoving;
            }
            emitter.setPosition(positionComponent.x + xOffset, positionComponent.y);
            emitter.draw(DrawUtil.batch, deltaTime);
        }
        
        particleComponent.effect.setPosition(positionComponent.x, positionComponent.y);
        particleComponent.effect.draw(DrawUtil.batch);
    }

    private boolean destroyIfFinished(Entity entity, ForegroundParticleComponent particleComponent) {
        if(particleComponent.killWhenFinished && particleComponent.effect.isComplete())
        {
            DeathEvent.emit(entity);
            return true;
        }

        return false;
    }

    private void reduceEmissionIfIdle(ForegroundParticleComponent particleComponent, ParticleEmitter emitter, boolean isMoving, int emitterIndex)
    {
        float highMax = emitter.getEmission().getHighMax();
        if(isMoving && highMax < particleComponent.startEmissionHighMax[emitterIndex])
        {
            emitter.getEmission().setHigh(particleComponent.startEmissionHighMin[emitterIndex], particleComponent.startEmissionHighMax[emitterIndex]);
            emitter.getEmission().setLow(particleComponent.startEmissionLowMin[emitterIndex], particleComponent.startEmissionLowMax[emitterIndex]);
            emitter.start();
        }
        else if(!isMoving && highMax > particleComponent.startEmissionHighMax[emitterIndex] * GameConstants.IDLE_PARTICLE_REDUCTION)
        {
            emitter.getEmission().setHigh(particleComponent.startEmissionHighMin[emitterIndex] * GameConstants.IDLE_PARTICLE_REDUCTION , particleComponent.startEmissionHighMax[emitterIndex] * GameConstants.IDLE_PARTICLE_REDUCTION);
            emitter.getEmission().setLow(particleComponent.startEmissionLowMin[emitterIndex] * GameConstants.IDLE_PARTICLE_REDUCTION, particleComponent.startEmissionLowMax[emitterIndex] * GameConstants.IDLE_PARTICLE_REDUCTION);
            emitter.start();
        }
    }

    private void flipHorizontal(ForegroundParticleComponent particleComponent)
    {
        Array<ParticleEmitter> emitters = particleComponent.effect.getEmitters();        
        for (int i = 0; i < emitters.size; i++) {                          
           ScaledNumericValue angle = emitters.get(i).getAngle();                                   
           angle.setHigh(180 - angle.getHighMin(), 180 - angle.getHighMax());

           RangedNumericValue xOffsetValue = emitters.get(i).getXOffsetValue();
           xOffsetValue.setLow(-xOffsetValue.getLowMin(), -xOffsetValue.getLowMax());
        }
        
        particleComponent.isFlippedHorizontal = !particleComponent.isFlippedHorizontal;
    }
}