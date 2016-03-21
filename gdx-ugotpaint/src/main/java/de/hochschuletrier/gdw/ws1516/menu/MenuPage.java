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

    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_WIDTH = 200;
    public static final int MENU_X = 50;
    public static final int MENU_Y = 470;
    public static final int MENU_STEP = 55;
    public static final int LABEL_STEP = 40;
    
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
    private final boolean drawPainted;

    public MenuPage(Skin skin, boolean drawPainted) {
        super();
        this.skin = skin;
        this.drawPainted = drawPainted;
        setVisible(false);
        
        clickSound = assetManager.getSound("click");
    }
    
    protected void addForeground() {
        overlayImage = new DecoImage(assetManager.getTexture("overlay"));
        addActor(overlayImage);
    }
    
    @Override
    public void draw (Batch batch, float parentAlpha) {
        canvas.render(drawPainted, batch, canvasPosition, canvasScale, false);
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

    protected final TextButton addLeftAlignedButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x, y, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.left);
        return button;
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
