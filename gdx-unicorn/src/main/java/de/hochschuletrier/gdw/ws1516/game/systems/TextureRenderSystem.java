package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;

public class TextureRenderSystem extends SubSystem {
    
    public TextureRenderSystem() {
        super(Family.all(TextureComponent.class, PositionComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TextureComponent texture = ComponentMappers.texture.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        if(texture.texture != null && position != null)
        {
            int w = texture.texture.getWidth();
            int h = texture.texture.getHeight();
            
            if(texture.flipHorizontal)
            {
                DrawUtil.batch.draw(texture.texture, position.x + w * 0.5f, position.y + h * 0.5f, -w, -h);
            }
            else
            {
                DrawUtil.batch.draw(texture.texture, position.x - w * 0.5f, position.y - h * 0.5f, w, h);
            }
        }
    }
}