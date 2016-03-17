package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.utils.Canvas;

public class MenuPage extends Group {

    protected Main main = Main.getInstance();
    protected AssetManagerX assetManager = main.getAssetManager();
    protected final Skin skin;
    protected final Vector2 canvasPosition = new Vector2(
            GameConstants.WINDOW_WIDTH * GameConstants.CANVAS_SCALE_MENU_REST,
            GameConstants.WINDOW_HEIGHT * GameConstants.CANVAS_SCALE_MENU_REST
    );
    protected float canvasScale = 0.715f;
    protected final Canvas canvas = Main.getCanvas();
    protected DecoImage overlayImage;
    private final Sound clickSound;

    public MenuPage(Skin skin) {
        super();
        this.skin = skin;
        setVisible(false);
        
        clickSound = assetManager.getSound("click");
    }
    
    protected void addForeground() {
        overlayImage = new DecoImage(assetManager.getTexture("overlay"));
        addActor(overlayImage);
    }
    
    @Override
    public void draw (Batch batch, float parentAlpha) {
        canvas.render(batch, canvasPosition, canvasScale, false);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        if (isVisible()) {
            super.act(delta);
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        if (clipBegin(0, 0, getWidth(), getHeight())) {
            super.drawChildren(batch, parentAlpha);
            clipEnd();
        }
    }

    protected final void addLeftAlignedButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x, y, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.left);
    }

    protected final void addCenteredButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x - width / 2, y - height / 2, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.center);
    }

    protected final TextButton addButton(int x, int y, int width, int height, String text, Runnable runnable, String style) {
        TextButton button = new TextButton(text, skin, style);
        button.setBounds(x, y, width, height);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                runnable.run();
            }
        });
        addActor(button);
        return button;
    }
}
