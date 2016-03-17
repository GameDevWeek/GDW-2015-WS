package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;

public class TextureRenderSystem extends SubSystem {
    private static final Logger logger = LoggerFactory.getLogger(TextureRenderSystem.class);
    
    public TextureRenderSystem() {
        super(Family.all(TextureComponent.class, PositionComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TextureComponent texture = ComponentMappers.texture.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        if(texture.texture != null && position != null) {
            drawRotated(texture.texture, position.x, position.y, position.rotation, texture.flipHorizontal,
                        texture.originX, texture.originY);
        }
    }
    
    private void drawRotated(Texture texture, float x, float y, float angleInDeg, boolean flipHorizontal, float originX, float originY) {
        
        float w = texture.getWidth();
        float h = texture.getHeight();
        
        DrawUtil.batch.draw(texture, x - w * 0.5f, y - h * 0.5f, originX, originY, w, h,
                1.0f, 1.0f, angleInDeg, 0, 0, (int) w, (int) h, flipHorizontal, false);
        
    }
    
}