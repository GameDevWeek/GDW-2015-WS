package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimator;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimatorActor;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimatorListener;
import de.hochschuletrier.gdw.ws1516.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuPageScene extends MenuPage implements SceneAnimator.Getter, SceneAnimatorListener {

    private static final Logger logger = LoggerFactory.getLogger(MenuPageScene.class);
    private final Hotkey increaseSpeed = new Hotkey(this::increaseSpeed, Input.Keys.PAGE_UP, HotkeyModifier.CTRL);
    private final Hotkey decreaseSpeed = new Hotkey(this::decreaseSpeed, Input.Keys.PAGE_DOWN, HotkeyModifier.CTRL);
    private final Hotkey resetSpeed = new Hotkey(this::resetSpeed, Input.Keys.HOME, HotkeyModifier.CTRL);

    private MenuManager menuManager;

    private SceneAnimator sceneAnimator;
    private final Runnable callback;

    public MenuPageScene(Skin skin, MenuManager menuManager, String filename, Runnable callback, String bg) {
        super(skin, bg);
        this.menuManager = menuManager;
        this.callback = callback;

        try {
            sceneAnimator = new SceneAnimator(this, filename);
            sceneAnimator.addListener(this);
            final SceneAnimatorActor sceneAnimatorActor = new SceneAnimatorActor(sceneAnimator);
            menuManager.getStage().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    sceneAnimator.abortPausePaths();
                }
            });
            addActor(sceneAnimatorActor);
        } catch (Exception ex) {
            logger.error("Error loading intro", ex);
        }

        addLeftAlignedButton(55, 40, 100, 50, "Menü", () -> menuManager.popPage(), "menu");

        addRightAlignedButton(Main.WINDOW_WIDTH - 155, 40, 100, 50, "Überspringen", this::skip, "buttonSound");
    }

    private void increaseSpeed() {
        sceneAnimator.setTimeFactor(Math.min(10.0f, sceneAnimator.getTimeFactor() + 1.f));
    }

    private void decreaseSpeed() {
        sceneAnimator.setTimeFactor(Math.max(0.0f, sceneAnimator.getTimeFactor() - 1.f));
    }

    private void resetSpeed() {
        sceneAnimator.setTimeFactor(1.0f);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE && increaseSpeed != null) {
            if (visible) {
                increaseSpeed.register();
                decreaseSpeed.register();
                resetSpeed.register();
            } else {
                increaseSpeed.unregister();
                decreaseSpeed.unregister();
                resetSpeed.unregister();
            }
        }
        if (sceneAnimator != null && visible) {
            sceneAnimator.reset();
        }
    }

    private void skip() {
        this.callback.run();
    }

    @Override
    public BitmapFont getFont(String name) {
        return skin.getFont(name);
    }

    @Override
    public AnimationExtended getAnimation(String name) {
        return assetManager.getAnimation(name);
    }

    @Override
    public Texture getTexture(String name) {
        return assetManager.getTexture(name);
    }

    @Override
    public void onSceneEnd() {
        skip();
    }

    @Override
    public Sound getSound(String name) {
        // temp fix for silent sounds
        final Sound sound = assetManager.getSound(name);
        SoundEmitter.playGlobal(sound, false);
        return null;
    }
}
