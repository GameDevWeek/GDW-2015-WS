package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.Canvas;

public class CanvasRenderSystem extends IteratingSystem {

    private final Canvas canvas = Main.getCanvas();

    public CanvasRenderSystem(int priority) {
        super(Family.all(PositionComponent.class, AnimationComponent.class, PlayerComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PositionComponent pos = ComponentMappers.position.get(entity);
        PlayerComponent player = ComponentMappers.player.get(entity);
        canvas.setColor(player.color.color);
        canvas.drawPoint(pos.pos);
        for (Vector2 segment : player.segments) {
            canvas.drawPoint(segment);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        canvas.render(true, DrawUtil.batch, Vector2.Zero, 1, true);
    }
}
