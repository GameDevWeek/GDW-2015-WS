package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1516.Cursor;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class MenuPageRoot extends MenuPage {
    private final Type type;
    private final MenuManager menuManager;
    private final DecoImage overlayImage2;

    public enum Type {

        MAINMENU,
        INGAME
    }
    
    private float canvasAnimationTime = 0;
    private final float totalCanvasAnimationTime = 1;
    private boolean bigCanvas = true;

    public MenuPageRoot(Skin skin, MenuManager menuManager, Type type) {
        super(skin, true);
        this.type = type;
        this.menuManager = menuManager;

//        addActor(new DecoImage(assetManager.getTexture("menu_bg_root_bottom")));
        int x = MENU_X;
        int i = 0;
        int y = MENU_Y;
        if (type == Type.MAINMENU) {
            addLeftAlignedButton(x, y - MENU_STEP * (i++), BUTTON_WIDTH, BUTTON_HEIGHT, "Spiel Starten", this::fadeToGame);
        } else {
            addLeftAlignedButton(x, y - MENU_STEP * (i++), BUTTON_WIDTH, BUTTON_HEIGHT, "Fortsetzen", this::fadeToGame);
        }
        addPageEntry(menuManager, x, y - MENU_STEP * (i++), BUTTON_WIDTH, "Credits", new MenuPageCredits(skin, menuManager));
        
        i++;
        if (type != Type.MAINMENU) {
            addLeftAlignedButton(x, y - MENU_STEP * (i++), BUTTON_WIDTH, BUTTON_HEIGHT, "Hauptmenü", this::stopGame);
        } else {
            addLeftAlignedButton(x, y - MENU_STEP * (i++), BUTTON_WIDTH, BUTTON_HEIGHT, "Beenden", ()->System.exit(0));
        }
        addForeground();
        overlayImage2 = new DecoImage(assetManager.getTexture("overlay_main"));
        addActor(overlayImage2);
    }

    @Override
    public void act(float delta) {
        if(isVisible()) {
            if (canvasAnimationTime < totalCanvasAnimationTime) {
                canvasAnimationTime += delta;
                if (canvasAnimationTime >= totalCanvasAnimationTime) {
                    canvasAnimationTime = totalCanvasAnimationTime;
                    if(bigCanvas)
                        this.showGame();
                    else {
                        overlayImage2.setVisible(type == Type.MAINMENU);
                        this.setTouchable(Touchable.enabled);
                    }
                }
            }

            float pct = canvasAnimationTime / totalCanvasAnimationTime;
            if (bigCanvas)
                pct = 1 - pct;
            float invPct = 1 - pct;
            canvasPosition.set(
                GameConstants.WINDOW_WIDTH * GameConstants.CANVAS_SCALE_MENU_REST  * pct,
                GameConstants.WINDOW_HEIGHT * GameConstants.CANVAS_SCALE_MENU_REST  * pct
            );
            canvasScale = GameConstants.CANVAS_SCALE_MENU + GameConstants.CANVAS_SCALE_MENU_REST * invPct;
            
            if(invPct > 0.75f) {
                setColor(1, 1, 1, 1 - (invPct - 0.75f)*4);
            } else {
                setColor(1, 1, 1, 1);
            }
            
            float dpct = Math.min(0.75f, invPct) / 0.75f;
            overlayImage.setPosition(0,
                    -invPct * GameConstants.WINDOW_HEIGHT * GameConstants.CANVAS_SCALE_MENU_REST
            );
            
            super.act(delta);
        }
    }

    public void fadeToMenu() {
        overlayImage2.setVisible(false);
        Cursor.enable();
        canvasAnimationTime = 0;
        bigCanvas = false;
        this.setTouchable(Touchable.disabled);
    }

    public void fadeToMenuInstant() {
        this.fadeToMenu();
        this.canvasAnimationTime = 0.99f;
        overlayImage2.setVisible(type == Type.MAINMENU);
    }
    
    public void fadeToGame() {
        overlayImage2.setVisible(false);
        canvasAnimationTime = 0;
        bigCanvas = true;
        this.setTouchable(Touchable.disabled);
    }
    
    private void showGame() {
        Cursor.disable();
        if (type == Type.MAINMENU) {
            Game game = new Game();
            game.init(assetManager);
            main.changeState(new GameplayState(assetManager, game));
        } else {
            menuManager.popPage();
        }
    }

    private void stopGame() {
        if (!main.isTransitioning()) {
            main.changeState(main.getPersistentState(MainMenuState.class));
        }
    }

    protected final void addPageEntry(MenuManager menuManager, int x, int y, int width, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, width, 40, text, () -> menuManager.pushPage(page));
    }
}
