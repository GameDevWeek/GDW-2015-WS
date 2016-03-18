package de.hochschuletrier.gdw.ws1516.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.audio.Wav;
import com.badlogic.gdx.backends.lwjgl.audio.Wav.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.PauseableThread;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.menu.EndPage;
import de.hochschuletrier.gdw.ws1516.menu.MainMenuPage;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.PauseGameEvent;


/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends BaseGameState implements GameOverEvent.Listener {


    private static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.5f);

    private final Game game;
    private final Music music;

    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, this::onMenuEmptyPop);
    private final InputForwarder inputForwarder;
    private final InputProcessor menuInputProcessor;
    private final InputProcessor gameInputProcessor;
    

    public GameplayState(AssetManagerX assetManager, Game game) {
        this.game = game;
        
        
        music = assetManager.getMusic("gameplaytheme");

        Skin skin = ((MainMenuState)Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
        final MainMenuPage menuPageRoot = new MainMenuPage(skin, menuManager, MainMenuPage.Type.PAUSED);

        menuManager.addLayer(menuPageRoot);   
        menuInputProcessor = menuManager.getInputProcessor();
        gameInputProcessor = game.getInputProcessor();


//        menuManager.addLayer(new DecoImage(assetManager.getTexture("menu_fg")));



        menuManager.pushPage(menuPageRoot);
        
               
 
        
//        menuManager.getStage().setDebugAll(true);


        Main.getInstance().addScreenListener(menuManager);

        inputForwarder = new InputForwarder() {

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (mainProcessor == gameInputProcessor) {
                        PauseGameEvent.change();
                        mainProcessor = menuInputProcessor;                        
                    } else {
                        PauseGameEvent.change();

                        menuManager.popPage();
                    }
                    return true;
                }
                return super.keyUp(keycode);
            }
        };
    }

    
    private void onMenuEmptyPop() {
        inputForwarder.set(gameInputProcessor);
    }
    

    @Override
    public void update(float delta) {
        game.update(delta);
        if (inputForwarder.get() == menuInputProcessor) {
            menuManager.update(delta);
            Main.getInstance().screenCamera.bind();
            DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), OVERLAY_COLOR);
            menuManager.render();
        }
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        //MusicManager.setGlobalVolume(0);
        System.out.println("enterGamestate");
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
        
        
    }

    @Override
    public void onEnterComplete() {
        
        System.out.println("enterGamestateComplete");
        Main.inputMultiplexer.addProcessor(inputForwarder);
        inputForwarder.set(gameInputProcessor);

        GameOverEvent.register(this);
        MusicManager.setGlobalVolume(0);

    }

    @Override
    public void onLeave(BaseGameState nextState) {
       
        Main.inputMultiplexer.removeProcessor(inputForwarder);
        GameOverEvent.unregister(this);
    }

    @Override
    public void dispose() {
        game.dispose();
    }

    /**
     * @author Tobi - Gamlogic 
     * made it a winningScreen upon winning
     */
    @Override
    public void onGameOverEvent(boolean won) {
        Skin skin = ((MainMenuState)Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
        EndPage endPage; 
        if ( won ){
            endPage = new EndPage(skin, menuManager, "transparent_bg", EndPage.Type.WIN);
        } else {
            endPage = new EndPage(skin, menuManager, "transparent_bg", EndPage.Type.GAMEOVER);
        }
        PauseGameEvent.emit(true);
            
        menuManager.addLayer(endPage);
        menuManager.pushPage(endPage);
        inputForwarder.set(menuInputProcessor);
            
    }
}
