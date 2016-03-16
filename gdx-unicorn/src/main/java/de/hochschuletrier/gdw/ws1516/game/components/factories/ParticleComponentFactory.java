package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.ParticleComponent;

public class ParticleComponentFactory extends ComponentFactory<EntityFactoryParam> 
{

    @Override
    public String getType() 
    {
        return "ParticleTest";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param)
    {
        ParticleComponent component = engine.createComponent(ParticleComponent.class);
        String var = properties.getString("effect");
        ParticleEffect var2 = assetManager.getParticleEffect(var);
        component.effect = new ParticleEffect(var2);
                
        entity.add(component);
    }
}