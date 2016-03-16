package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class CanvasRenderSystem extends IteratingSystem {
    
    Pixmap pixmap = new Pixmap(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, Pixmap.Format.RGBA8888);
    private final Color drawTint = new Color(1,1,1,0.5f);
    private final Color clearColor = new Color(0,0,0,0);
    private final Color pixelColor = new Color();
    private final Texture texture = new Texture(pixmap);
    private float pctFilled;
    private float nextUpdate = 0;

    public CanvasRenderSystem(int priority) {
        super(Family.all(PositionComponent.class, AnimationComponent.class, PlayerComponent.class).get(), priority);
    }

    public float getPctFilled() {
        return pctFilled;
    }
    
    public void clear() {
        pctFilled = 0;
        pixmap.setColor(clearColor);
        pixmap.fill();
    }

    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PositionComponent pos = ComponentMappers.position.get(entity);
        Vector2 position = pos.pos;
        AnimationComponent animation = ComponentMappers.animation.get(entity);
        PlayerComponent player = ComponentMappers.player.get(entity);
        pixmap.setColor(animation.tint);
        pixmap.fillCircle((int)position.x, Main.WINDOW_HEIGHT - (int)position.y, GameConstants.PAINT_RADIUS);
        for (Vector2 segment : player.segments) {
            pixmap.fillCircle((int)segment.x, Main.WINDOW_HEIGHT - (int)segment.y, GameConstants.PAINT_RADIUS);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        final int w = pixmap.getWidth();
        final int h = pixmap.getHeight();
        
        texture.draw(pixmap, 0, 0);
        DrawUtil.setColor(drawTint);
        DrawUtil.batch.draw(texture, 0, 0, w, h);
        DrawUtil.resetColor();
        
        // Only update pctFilled every second
        nextUpdate -= deltaTime;
        if(nextUpdate <= 0) {
            nextUpdate = 1;
            float drawnPixels = 0;
            float totalPixels = w * h;
            for(int x=0; x<w; x++) {
                for(int y=0; y<h; y++) {
                    pixelColor.set(pixmap.getPixel(x, y));
                    if(pixelColor.a > 0)
                        drawnPixels++;
                }
            }
            pctFilled =  drawnPixels / totalPixels;
        }
    }
}
