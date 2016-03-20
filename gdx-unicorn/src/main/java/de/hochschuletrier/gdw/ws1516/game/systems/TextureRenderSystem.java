package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.SafePointTextureComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;

public class TextureRenderSystem extends SubSystem {
    private static final Logger logger = LoggerFactory.getLogger(TextureRenderSystem.class);
    
    @SuppressWarnings("unchecked")
    public TextureRenderSystem() {
        super(Family.all(PositionComponent.class).one(TextureComponent.class, SafePointTextureComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TextureComponent texture = ComponentMappers.texture.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        if(texture == null) {
            texture = ComponentMappers.safePointTexture.get(entity);
        }
        
        if(texture.texture != null && position != null) {
            drawRotated(texture.texture, position.x, position.y, position.rotation,
                        texture.flipHorizontal, texture.flipVertical,
                        texture.originX, texture.originY, texture.alpha);
        }
    }
    
    private void drawRotated(Texture texture, 
                             float x, float y, float angleInDeg,
                             boolean flipHorizontal, boolean flipVertical,
                             float originX, float originY, float alpha) {
        
        float w = texture.getWidth();
        float h = texture.getHeight();
        
        if(alpha < 1f && alpha >= 0f)
        {
            ShaderProgram alphaTextureShader = ShaderLoader.getAlphaTextureShader();
            DrawUtil.setShader(alphaTextureShader);
            alphaTextureShader.setUniformf("u_alpha", alpha);
        }
        
    	DrawUtil.batch.draw(texture, x - w*0.5f, y - h*0.5f, w*0.5f, h*0.5f, w, h, 1, 1, angleInDeg, 
    			0, 0, (int) w, (int) h, flipHorizontal, flipVertical);
        
        if(alpha < 1f && alpha >= 0f)
        {
            DrawUtil.setShader(null);
        }
    }
    
}