package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class AnimationRenderSystem extends IteratingSystem {

    private Color color = new Color(1,1,1,1);

    public AnimationRenderSystem(int priority) {
        super(Family.all(PositionComponent.class, AnimationComponent.class).get(), priority);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animation = ComponentMappers.animation.get(entity);
        PositionComponent pos = ComponentMappers.position.get(entity);
        PlayerComponent player = ComponentMappers.player.get(entity);

        animation.stateTime += deltaTime;

        color.a = animation.alpha;
        DrawUtil.setColor(color);
        drawAnimation(animation, pos.pos);
        
        if(player != null) {
            for (Vector2 segment : player.segments) {
                drawAnimation(animation, segment);
            }
        }
        DrawUtil.resetColor();
    }

    private void drawAnimation(AnimationComponent animation, Vector2 position) {
        TextureRegion keyFrame = animation.animation.getKeyFrame(animation.stateTime);
        int w = keyFrame.getRegionWidth();
        int h = keyFrame.getRegionHeight();
        DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, animation.rotation);
    }
}
