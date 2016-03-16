package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

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
        
        entity.add(component);
    }

}
