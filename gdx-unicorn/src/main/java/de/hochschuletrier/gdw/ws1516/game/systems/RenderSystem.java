package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.EndFlyEvent;
import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.events.JumpEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.StartFlyEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.RenderLayerComponent;

public class RenderSystem extends SortedSubIteratingSystem {
    
    private final static RenderComparator renderComparator = new RenderComparator();

    private BackgroundParticleRenderSystem backgroundParticleRenderSystem;
    private AnimationRenderSystem animationRenderSystem;
    private ForegroundParticleRenderSystem particleRenderSystem;
    
    @SuppressWarnings("unchecked")
    public RenderSystem(int priority) {
        super(Family.all(PositionComponent.class, RenderLayerComponent.class).get(), renderComparator, priority);

        backgroundParticleRenderSystem = new BackgroundParticleRenderSystem();
        addSubSystem(backgroundParticleRenderSystem);
        animationRenderSystem = new AnimationRenderSystem();
        addSubSystem(animationRenderSystem);
        addSubSystem(new TextureRenderSystem());
        particleRenderSystem = new ForegroundParticleRenderSystem();
        addSubSystem(particleRenderSystem);
    }
    
    @Override
    public void addedToEngine(Engine engine) 
    {
        super.addedToEngine(engine);
        EnemyActionEvent.register(animationRenderSystem);
        HornAttackEvent.register(animationRenderSystem);
        MovementStateChangeEvent.register(animationRenderSystem);
        PlayerStateChangeEvent.register(animationRenderSystem);
    }
    
    @Override
    public void removedFromEngine(Engine engine) 
    {
        super.removedFromEngine(engine);
        EnemyActionEvent.unregister(animationRenderSystem);
        HornAttackEvent.unregister(animationRenderSystem);
        MovementStateChangeEvent.unregister(animationRenderSystem);
        PlayerStateChangeEvent.unregister(animationRenderSystem);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        // preprocessing if necessary
        super.processEntity(entity, deltaTime);
        // postprocessing if necessary
    }
    
    @Override
    public void update(float deltaTime) {
        // preprocessing if necessary
        super.update(deltaTime);
        // postprocessing if necessary
    }

    private static final class RenderComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            RenderLayerComponent l1 = ComponentMappers.renderLayer.get(e1);
            RenderLayerComponent l2 = ComponentMappers.renderLayer.get(e2);

            return l1.layer - l2.layer;
        }
     }
}
