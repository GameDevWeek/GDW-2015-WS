package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;
import de.hochschuletrier.gdw.ws1516.events.SplashEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.SplashComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

/**
 * Created by glumbatsch on 17.03.2016.
 */
public class SplashSystem extends IteratingSystem implements SplashEvent.Listener {

    private final Game game;

    private Engine engine;

    public SplashSystem(Game game) {
        this(game, 0);
    }

    public SplashSystem(Game game, int priority) {
        super(Family.one(SplashComponent.class).get(),priority);
        this.game = game;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        SplashEvent.register(this);
        this.engine = engine;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        SplashEvent.unregister(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        position.pos.y += 100*deltaTime;

        // if animation has played half way through fade the entity out and eventually destroy it
        AnimationComponent anim = ComponentMappers.animation.get(entity);
        if(anim.stateTime > anim.animation.animationDuration/2){
            anim.alpha = Math.max(anim.alpha - deltaTime,0);
        }
        if(anim.alpha <= 0){
            engine.removeEntity(entity);
        }
    }

    @Override
    public void onSplashEvent(Vector2 first, PlayerColor color) {
        // Create entity and set it's animation to the splash animation
        Entity entity = game.createEntity("splash",first.x,first.y,color);
        AnimationComponent anim = ComponentMappers.animation.get(entity);
        anim.animation = color.splashAnimation;
        
        SoundEvent.emit(entity, "splash");
    }
}
