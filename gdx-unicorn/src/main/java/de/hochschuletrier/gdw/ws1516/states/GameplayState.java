package de.hochschuletrier.gdw.ws1516.states;

import org.slf4j.LoggerFactory;

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
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.PauseableThread;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.Settings;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.menu.EndPage;
import de.hochschuletrier.gdw.ws1516.menu.MainMenuPage;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent.GameStateType;
import de.hochschuletrier.gdw.ws1516.events.FinalScoreEvent;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;


/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends BaseGameState implements GameOverEvent.Listener, FinalScoreEvent.Listener, RainbowEvent.Listener  {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Game.class);

    private static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.5f);
    
    private ScoreComponent scoreComp;
    

    private final Game game;
    private final Music music;
    private final Music rainbowMusic;

    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, this::onMenuEmptyPop);
    private final InputForwarder inputForwarder;
    private final InputProcessor menuInputProcessor;
    private final InputProcessor gameInputProcessor;
    
    private float generalVolume;
    private float musicVolume;
    
    

    public GameplayState(AssetManagerX assetManager, Game game, Music music) {
        this.game = game;
        
        this.music = music;
        rainbowMusic = assetManager.getMusic("rainbowtheme");

        Skin skin = ((MainMenuState)Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
        final MainMenuPage menuPageRoot = new MainMenuPage(skin, menuManager, MainMenuPage.Type.PAUSED);

        menuManager.addLayer(menuPageRoot);   
        menuInputProcessor = menuManager.getInputProcessor();
        gameInputProcessor = game.getInputProcessor();


        //menuManager.addLayer(new DecoImage(assetManager.getTexture("tutorial")));



        menuManager.pushPage(menuPageRoot);
        
               
 
        
//        menuManager.getStage().setDebugAll(true);


        Main.getInstance().addScreenListener(menuManager);

        inputForwarder = new InputForwarder() {

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (mainProcessor == gameInputProcessor) {
                        mainProcessor = menuInputProcessor;                        
                    } else {
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
            if ( Game.isInState( GameStateType.GAME_PLAYING)  )
            {   /** @author Tobi-Gamelogic   hier weis ich ganz sicher dass es pasuiert ist **/
                ChangeInGameStateEvent.emit(GameStateType.GAME_PAUSE);
            }
        }else
        {
            if ( Game.isInState( GameStateType.GAME_PAUSE ) )
            {
                ChangeInGameStateEvent.emit(GameStateType.GAME_PLAYING );
            }
        }
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        System.out.println("enterGamestate");
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
        
        
    }

    @Override
    public void onEnterComplete() {
        
        System.out.println("enterGamestateComplete");
        Main.inputMultiplexer.addProcessor(inputForwarder);
        inputForwarder.set(gameInputProcessor);
        GameOverEvent.register(this);
        FinalScoreEvent.register(this);
        RainbowEvent.register(this);
       

    }

    @Override
    public void onLeave(BaseGameState nextState) {
       
        Main.inputMultiplexer.removeProcessor(inputForwarder);
        GameOverEvent.unregister(this);
        FinalScoreEvent.unregister(this);
        RainbowEvent.unregister(this);
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
            endPage = new EndPage(skin, menuManager, "transparent_bg", EndPage.Type.WIN, scoreComp);
        } else {
            endPage = new EndPage(skin, menuManager, "transparent_bg", EndPage.Type.GAMEOVER, scoreComp);
        }
        ChangeInGameStateEvent.emit(GameStateType.GAME_PAUSE);
            
        menuManager.addLayer(endPage);
        menuManager.pushPage(endPage);
        inputForwarder.set(menuInputProcessor);
            
    }


    @Override
    public void onFinalScoreChanged(long score, ScoreComponent scoreComponent) {
        this.scoreComp=scoreComponent;
        
    }


    @Override
    public void onRainbowCollect(Entity player) {
        MusicManager.play(rainbowMusic, GameConstants.MUSIC_FADE_TIME);
              
    }


    @Override
    public void onRainbowModeEnd(Entity player) {
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
             
    }
}
