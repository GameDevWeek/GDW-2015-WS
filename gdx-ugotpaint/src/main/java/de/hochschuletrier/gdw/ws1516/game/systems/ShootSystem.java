package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import de.hochschuletrier.gdw.ws1516.events.ShootEvent;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.*;

public class ShootSystem extends EntitySystem implements ShootEvent.Listener {

    private final Game game;

    public ShootSystem(Game game, int priority) {
        super(priority);
        this.game = game;
    }

    @Override
    public void addedToEngine(Engine engine) {
        ShootEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        ShootEvent.unregister(this);
    }

    @Override
    public void onShootEvent(Entity playerEntity) {
        PlayerComponent player = ComponentMappers.player.get(playerEntity);
        if(player.segments.size() > 2) {
            InputComponent input = ComponentMappers.input.get(playerEntity);
            final PositionComponent pos = ComponentMappers.position.get(playerEntity);

            float x = pos.pos.x + input.lastMoveDirection.x * GameConstants.SEGMENT_DISTANCE;
            float y = pos.pos.y + input.lastMoveDirection.y * GameConstants.SEGMENT_DISTANCE;
            
            Entity entity = game.createEntity("projectile", x, y, player.color);
            
            SoundEvent.emit(entity, "shoot");
            ProjectileComponent projectile = ComponentMappers.projectile.get(entity);
            projectile.velocity.set(input.lastMoveDirection).nor().scl(120);

            ParticleComponent particle = ComponentMappers.particle.get(entity);
            particle.effect = new ParticleEffect(player.color.particleEffectSplash);

            AnimationComponent animation = ComponentMappers.animation.get(entity);
            animation.animation = player.color.projectileAnimation;
            animation.rotation = projectile.velocity.angle();
            
            pos.pos.set(player.removeFirstSegments(1));
        }
    }
}
