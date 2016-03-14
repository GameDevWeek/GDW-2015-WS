package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.RenderLayerComponent;

public class RenderSystem extends SortedSubIteratingSystem {
    
    private final static RenderComparator renderComparator = new RenderComparator();

    @SuppressWarnings("unchecked")
    public RenderSystem(int priority) {
        super(Family.all(PositionComponent.class, RenderLayerComponent.class).get(), renderComparator, priority);

        addSubSystem(new AnimationRenderSystem());
        addSubSystem(new ParticleRenderSystem());
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
