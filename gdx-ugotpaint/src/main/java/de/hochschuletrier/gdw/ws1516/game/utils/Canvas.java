package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;

public class Canvas {
    
    Pixmap pixmap = new Pixmap(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT, Pixmap.Format.RGBA8888);
    private final Color drawTint = new Color(1,1,1,0.8f);
    private final Color clearColor = new Color(0,0,0,0);
    private final Color pixelColor = new Color();
    private final Texture texture = new Texture(pixmap);
    private float pctFilled;
    private final Texture background;
    private final Texture wall;
    
    public Canvas(AssetManagerX assetManager) {
        wall = assetManager.getTexture("wall");
        background = assetManager.getTexture("canvas");
    }
    
    public float getPctFilled() {
        return pctFilled;
    }

    public void clear() {
        pctFilled = 0;
        pixmap.setColor(clearColor);
        pixmap.fill();
    }

    public void setColor(Color tint) {
        pixmap.setColor(tint);
    }
    
    public void drawPoint(Vector2 position) {
        pixmap.fillCircle((int)position.x, (int)position.y, GameConstants.PAINT_RADIUS);
    }
    
    public void render(Batch batch, Vector2 pos, float scale, boolean flipY) {
        texture.draw(pixmap, 0, 0);
        
        drawTexture(wall, batch, 0, 0, 1, flipY);
        drawTexture(background, batch, pos.x, pos.y, scale, flipY);
        batch.setColor(drawTint);
        drawTexture(texture, batch, pos.x, pos.y, scale, flipY);
        batch.setColor(Color.WHITE);
    }

    private void drawTexture(Texture texture, Batch batch, float x, float y, float scale, boolean flipY) {
        float w = texture.getWidth();
        float h = texture.getHeight();
        batch.draw(texture, x, y, 0, 0, w, h, scale, scale, 0, 0, 0, (int)w, (int)h, false, flipY);
    }

    public void updatePctFilled() {
        final int w = pixmap.getWidth();
        final int h = pixmap.getHeight();
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
