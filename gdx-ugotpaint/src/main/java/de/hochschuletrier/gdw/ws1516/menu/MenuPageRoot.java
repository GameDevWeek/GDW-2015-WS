package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class MenuPageRoot extends MenuPage {
    private final Type type;
    private final MenuManager menuManager;

    public enum Type {

        MAINMENU,
        INGAME
    }
    
    private float canvasAnimationTime = 0;
    private final float totalCanvasAnimationTime = 1;
    private boolean bigCanvas = true;

    public MenuPageRoot(Skin skin, MenuManager menuManager, Type type) {
        super(skin);
        this.type = type;
        this.menuManager = menuManager;

//        addActor(new DecoImage(assetManager.getTexture("menu_bg_root_bottom")));
        int x = 50;
        int i = 0;
        int y = 370;
        int yStep = 55;
        if (type == Type.MAINMENU) {
            addLeftAlignedButton(x, y - yStep * (i++), 300, 50, "Spiel Starten", this::fadeToGame);
        } else {
            addLeftAlignedButton(x, y - yStep * (i++), 300, 50, "Fortsetzen", this::fadeToGame);
            addLeftAlignedButton(x, y - yStep * (i++), 300, 50, "Spiel verlassen", this::stopGame);
        }
        addPageEntry(menuManager, x, y - yStep * (i++), "Credits", new MenuPageCredits(skin, menuManager));
        addForeground();
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
                    else
                        this.setTouchable(Touchable.enabled);
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
            }
            
            float dpct = Math.min(0.75f, invPct) / 0.75f;
            overlayImage.setPosition(0,
                    -invPct * GameConstants.WINDOW_HEIGHT * GameConstants.CANVAS_SCALE_MENU_REST
            );
            
            super.act(delta);
        }
    }

    public void fadeToMenu() {
        canvasAnimationTime = 0;
        bigCanvas = false;
        this.setTouchable(Touchable.disabled);
    }
    
    public void fadeToGame() {
        canvasAnimationTime = 0;
        bigCanvas = true;
        this.setTouchable(Touchable.disabled);
    }
    
    private void showGame() {
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

    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 300, 40, text, () -> menuManager.pushPage(page));
    }
}
