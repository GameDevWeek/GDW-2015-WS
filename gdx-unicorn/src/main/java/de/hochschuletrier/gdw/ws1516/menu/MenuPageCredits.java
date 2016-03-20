package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimator;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimatorActor;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimatorListener;
import de.hochschuletrier.gdw.ws1516.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuPageCredits extends MenuPage implements SceneAnimator.Getter, SceneAnimatorListener {

    private static final Logger logger = LoggerFactory.getLogger(MenuPageCredits.class);
    private final Hotkey increaseSpeed = new Hotkey(this::increaseSpeed, Input.Keys.PAGE_UP, HotkeyModifier.CTRL);
    private final Hotkey decreaseSpeed = new Hotkey(this::decreaseSpeed, Input.Keys.PAGE_DOWN, HotkeyModifier.CTRL);
    private final Hotkey resetSpeed = new Hotkey(this::resetSpeed, Input.Keys.HOME, HotkeyModifier.CTRL);

    private MenuManager menuManager;

    private SceneAnimator sceneAnimator;

    private void increaseSpeed() {
        sceneAnimator.setTimeFactor(Math.min(10.0f, sceneAnimator.getTimeFactor() + 1.f));
    }

    private void decreaseSpeed() {
        sceneAnimator.setTimeFactor(Math.max(0.0f, sceneAnimator.getTimeFactor() - 1.f));
    }

    private void resetSpeed() {
        sceneAnimator.setTimeFactor(1.0f);
    }

    public MenuPageCredits(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        this.menuManager = menuManager;
        try {
            sceneAnimator = new SceneAnimator(this, "data/json/credits.json");
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
            logger.error("Error loading credits", ex);
        }

        addLeftAlignedButton(55, 40, 100, 50, "Menu", () -> menuManager.popPage(),"buttonSound");
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE && increaseSpeed != null) {
            if(visible) {
                increaseSpeed.register();
                decreaseSpeed.register();
                resetSpeed.register();
            } else {
                increaseSpeed.unregister();
                decreaseSpeed.unregister();
                resetSpeed.unregister();
            }
        }
        if(sceneAnimator != null && visible)
            sceneAnimator.reset();
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
        sceneAnimator.reset();
        menuManager.popPage();
    }

    @Override
    public Sound getSound(String name) {
        return assetManager.getSound(name);
    }
}
