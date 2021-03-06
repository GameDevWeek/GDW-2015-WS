package de.hochschuletrier.gdw.ws1516.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.ShowWinScreenEvent;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;
import de.hochschuletrier.gdw.ws1516.menu.MenuPageRoot;

/**
 * Gameplay state
 *
 * @author Santo Pfingsten
 */
public class GameplayState extends BaseGameState implements ShowWinScreenEvent.Listener {

    private static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.5f);

    private final Game game;

    private final MenuManager menuManager = new MenuManager(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT, this::onMenuEmptyPop);
    private final InputForwarder inputForwarder;
    private final InputProcessor menuInputProcessor;
    private final InputProcessor gameInputProcessor;
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
    private final BitmapFont font;
    private final MenuPageRoot menuPageRoot;
    private final Skin skin;


    public GameplayState(AssetManagerX assetManager, Game game) {
        font = assetManager.getFont("verdana_32");
        this.game = game;

        skin = ((MainMenuState) Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
        menuPageRoot = new MenuPageRoot(skin, menuManager, MenuPageRoot.Type.INGAME);
        menuManager.addLayer(menuPageRoot);
        menuInputProcessor = menuManager.getInputProcessor();
        gameInputProcessor = game.getInputProcessor();

        menuManager.pushPage(menuPageRoot);
//        menuManager.getStage().setDebugAll(true);

        Main.getInstance().addScreenListener(menuManager);

        inputForwarder = new InputForwarder() {

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (mainProcessor == gameInputProcessor) {
                        mainProcessor = menuInputProcessor;
                        menuPageRoot.fadeToMenu();
                    } else {
                        menuPageRoot.fadeToGame();
                    }
                    return true;
                }
                return super.keyUp(keycode);
            }
        };
        ShowWinScreenEvent.register(this);
    }

    private void onMenuEmptyPop() {
        inputForwarder.set(gameInputProcessor);
    }

    @Override
    public void update(float delta) {
        fpsCalc.addFrame();
        game.update(delta);
        if (inputForwarder.get() == menuInputProcessor) {
            menuManager.update(delta);
            Main.getInstance().screenCamera.bind();
            DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), OVERLAY_COLOR);
            menuManager.render();
        }

        Main.getInstance().screenCamera.bind();
        font.setColor(Color.WHITE);
        font.draw(DrawUtil.batch, String.format("%.2f FPS", fpsCalc.getFps()), 0, 0);
    }

    @Override
    public void onEnterComplete() {
        Main.inputMultiplexer.addProcessor(inputForwarder);
        inputForwarder.set(gameInputProcessor);
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        Main.inputMultiplexer.removeProcessor(inputForwarder);
        Main.getCanvas().clear();
    }

    @Override
    public void dispose() {
        ShowWinScreenEvent.unregister(this);
        game.dispose();
    }

    @Override
    public void onShowWinScreenEvent(PlayerColor color, float pctWinnerFilled) {
        menuManager.popAllPages();
        switch(color) {
            case RED:
                menuPageRoot.setWinMessage( "Spieler Rot hat gewonnen!", pctWinnerFilled);
                break;
            case BLUE:
                menuPageRoot.setWinMessage( "Spieler Blau hat gewonnen!", pctWinnerFilled);
                break;
            case NEUTRAL:
                menuPageRoot.setWinMessage( "Unentschieden!", pctWinnerFilled);
                break;
            default:
                throw new AssertionError(color.name());
            
        }
        menuManager.pushPage(menuPageRoot);
        
        inputForwarder.set(menuInputProcessor);
        menuPageRoot.fadeToMenu();
    }
}
