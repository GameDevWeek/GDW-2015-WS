package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.BackgroundParticleComponent;

public class BackgroundParticleComponentFactory extends ComponentFactory<EntityFactoryParam>
{

    @Override
    public String getType() 
    {
        return "BackgroundParticle";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) 
    {
        BackgroundParticleComponent component = engine.createComponent(BackgroundParticleComponent.class);
        
        component.effect = new ParticleEffect(assetManager.getParticleEffect(properties.getString("effect")));
        int numOfEmitters = component.effect.getEmitters().size;
        component.startEmissionHighMax = new float[numOfEmitters];
        component.startEmissionHighMin = new float[numOfEmitters];
        component.startEmissionLowMax = new float[numOfEmitters];
        component.startEmissionLowMin = new float[numOfEmitters];
        for(int i = 0; i < numOfEmitters; ++i)
        {
            ParticleEmitter emitter = component.effect.getEmitters().get(i);
            emitter.start();
            component.startEmissionHighMax[i] = emitter.getEmission().getHighMax();
            component.startEmissionHighMin[i] = emitter.getEmission().getHighMin();
            component.startEmissionLowMax[i] = emitter.getEmission().getLowMax();
            component.startEmissionLowMin[i] = emitter.getEmission().getLowMin();
        }
        component.isFlippedHorizontal = properties.getBoolean("flipHorizontal", false);
        component.flipVertical = properties.getBoolean("flipVertical", false);
        component.reduceEmissionIfIdle = properties.getBoolean("reduceEmissionIfIdle", false);
        component.offsetWhenMoving = properties.getFloat("offsetWhenMoving", 0);
        component.killWhenFinished = properties.getBoolean("kill_when_finished", false);
        
        entity.add(component);
    }

}
