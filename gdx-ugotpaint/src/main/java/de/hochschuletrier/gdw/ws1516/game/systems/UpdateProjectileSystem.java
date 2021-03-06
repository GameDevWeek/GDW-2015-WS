package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.events.EndgameEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ProjectileComponent;

public class UpdateProjectileSystem extends IteratingSystem implements EndgameEvent.Listener {

    private final Vector2 vel = new Vector2();
    private float speedFactor = GameConstants.GAME_SPEEDFACTOR;

    private Engine engine;

    public UpdateProjectileSystem() {
        this(0);
    }

    public UpdateProjectileSystem(int priority) {
        super(Family.all(ProjectileComponent.class).get(), priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        EndgameEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        EndgameEvent.unregister(this);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        ProjectileComponent projectile = ComponentMappers.projectile.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        AnimationComponent anim = ComponentMappers.animation.get(entity);
        vel.set(projectile.velocity).scl(deltaTime * speedFactor);
        position.pos.add(vel);

        // remove entity from engine if it is completely faded out
        if(anim.alpha <= 0){
            engine.removeEntity(entity);
        }
        // fade out entity after it left the canvas
        else if(position.pos.x < GameConstants.BOUND_LEFT||position.pos.x > GameConstants.BOUND_RIGHT
                || position.pos.y  < GameConstants.BOUND_TOP || position.pos.y > GameConstants.BOUND_BOTTOM){
            anim.alpha = Math.max(anim.alpha-2*deltaTime,0);
        }


    }

    @Override
    /**
     * Speeds up the players by given amount during the endgame.
     */
    public void onEndgameEvent() {
        speedFactor = GameConstants.ENDGAME_SPEEDFACTOR;
    }
}
