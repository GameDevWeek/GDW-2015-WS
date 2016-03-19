package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.events.EndFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;
import de.hochschuletrier.gdw.ws1516.events.StartFlyEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.UnicornAnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.UnicornAnimationComponent.UnicornColor;

public class AnimationEventHandlerSystem extends IteratingSystem implements RainbowEvent.Listener, StartFlyEvent.Listener, EndFlyEvent.Listener {

    public AnimationEventHandlerSystem(int priority) {
        super(Family.all().get(), priority);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        RainbowEvent.register(this);
        StartFlyEvent.register(this);
        EndFlyEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        RainbowEvent.unregister(this);
        StartFlyEvent.unregister(this);
        EndFlyEvent.unregister(this);
    }

    @Override
    public void onEndFlyEvent(Entity player) {
        UnicornAnimationComponent unicornAnimation = ComponentMappers.unicornAnimation.get(player);
        if(unicornAnimation != null && !unicornAnimation.isInRainbowMode)
        {
            unicornAnimation.switchUnicornColor(UnicornColor.pink);
        }
    }

    @Override
    public void onStartFlyEvent(Entity player, float time)
    {
        UnicornAnimationComponent unicornAnimation = ComponentMappers.unicornAnimation.get(player);
        if(unicornAnimation != null && !unicornAnimation.isInRainbowMode)
        {
            unicornAnimation.switchUnicornColor(UnicornColor.blue);
        }
    }

    @Override
    public void onRainbowCollect(Entity player)
    {
        UnicornAnimationComponent unicornAnimation = ComponentMappers.unicornAnimation.get(player);
        if(unicornAnimation != null)
        {
            unicornAnimation.switchUnicornColor(UnicornColor.rainbow);
        }
    }

    @Override
    public void onRainbowModeEnd(Entity player)
    {
        UnicornAnimationComponent unicornAnimation = ComponentMappers.unicornAnimation.get(player);
        if(unicornAnimation != null && !unicornAnimation.isInBlueMode)
        {
            unicornAnimation.switchUnicornColor(UnicornColor.blue);
        }
        else if(unicornAnimation != null)
        {
            unicornAnimation.switchUnicornColor(UnicornColor.pink);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Nothing todo
    }
}