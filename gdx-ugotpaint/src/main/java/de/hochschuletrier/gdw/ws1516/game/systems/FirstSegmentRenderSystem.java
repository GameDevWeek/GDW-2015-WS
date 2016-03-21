package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class FirstSegmentRenderSystem extends IteratingSystem {

    private final Texture texture;

    public FirstSegmentRenderSystem(int priority) {
        super(Family.all(PositionComponent.class, PlayerComponent.class).get(), priority);
        texture = Main.getInstance().getAssetManager().getTexture("first_segment_overlay");
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = ComponentMappers.position.get(entity);
        float x = pos.pos.x - texture.getWidth()*0.5f;
        float y = pos.pos.y - texture.getHeight()*0.5f;
        DrawUtil.draw(texture, x, y);
    }
}
