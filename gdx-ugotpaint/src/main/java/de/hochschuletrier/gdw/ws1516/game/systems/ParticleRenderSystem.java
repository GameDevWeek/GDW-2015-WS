package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.events.SplashEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.ParticleComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class ParticleRenderSystem extends IteratingSystem implements SplashEvent.Listener {

    private Engine engine;
    private Game game;

    public ParticleRenderSystem(Game game, int priority) {
        super(Family.one(ParticleComponent.class).get(), priority);
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

        ParticleComponent particle = ComponentMappers.particle.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);

        for (ParticleEmitter emitter : particle.effect.getEmitters()) {
            emitter.start();

            emitter.setPosition(position.pos.x, position.pos.y);
            emitter.draw(DrawUtil.batch, deltaTime);
        }


        particle.effect.setPosition(position.pos.x, position.pos.y);
        particle.effect.draw(DrawUtil.batch);

        boolean complete = false;
        for (ParticleEmitter emitter : particle.effect.getEmitters()) {
            if(!emitter.isContinuous() && emitter.getPercentComplete() <= -25) {
                complete = true;
                break;
            }
        }
        if(complete){
            engine.removeEntity(entity);
        }
    }

    @Override
    public void onSplashEvent(Vector2 first, PlayerColor color) {
        Entity entity = game.createEntity("explosion", first.x, first.y, color);
        ParticleComponent particle = ComponentMappers.particle.get(entity);

        particle.effect = new ParticleEffect(color.particleEffectExplosion);
        particle.effect.allowCompletion();

        engine.addEntity(entity);
    }
}
