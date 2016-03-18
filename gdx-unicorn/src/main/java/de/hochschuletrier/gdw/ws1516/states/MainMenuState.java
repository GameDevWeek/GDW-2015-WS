package de.hochschuletrier.gdw.ws1516.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent.ScoreType;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.menu.EndPage;
import de.hochschuletrier.gdw.ws1516.menu.MainMenuPage;
import de.hochschuletrier.gdw.ws1516.menu.MenuPageOptions;
import de.hochschuletrier.gdw.ws1516.menu.MenuPageRoot;
import de.hochschuletrier.gdw.ws1516.menu.Settings;


/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends BaseGameState {

    private final Skin skin = new Skin(Gdx.files.internal("data/ui/menu/skins/menu.json"));
    private final Music music;
    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, null);
    private final InputForwarder inputForwarder;
    

    public MainMenuState(AssetManagerX assetManager) {
        music = assetManager.getMusic("menutheme");

        //soundSlider = new SoundSlider();
        final MainMenuPage menuPageRoot = new MainMenuPage(skin, menuManager, MainMenuPage.Type.MENU);
   //     final EndPage menuPageRoot = new EndPage(skin, menuManager, "transparent_bg");
        menuManager.addLayer(menuPageRoot);
        
    //  menuManager.addLayer(new DecoImage(assetManager.getTexture("tutorial")));
        menuManager.pushPage(menuPageRoot);
//        menuManager.getStage().setDebugAll(true);

        Main.getInstance().addScreenListener(menuManager);

        inputForwarder = new InputForwarder() {
            @Override
            public boolean keyUp(int keycode) {
                if (mainProcessor != null && keycode == Input.Keys.ESCAPE) {
                    menuManager.popPage();
                    return true;
                }
                return super.keyUp(keycode);
            }
        };

        Main.inputMultiplexer.addProcessor(inputForwarder);
    }

    public void render() {
        Main.getInstance().screenCamera.bind();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);
        menuManager.render();
    }

    @Override
    public void update(float delta) {
        menuManager.update(delta);
        render();
    }

    @Override
    public void onEnterComplete() {
       
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
        
        MusicManager.setGlobalVolume(Settings.musicValue.get());
       
        
        inputForwarder.set(menuManager.getInputProcessor());
      //  music.setVolume(0.5F);
        menuManager.popAllPages();
        
     //   music.play();
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        inputForwarder.set(null);
       // MusicManager.stop();
    }

    @Override
    public void dispose() {
        menuManager.dispose();
    }

    /**
     * @return the skin
     */
    public Skin getSkin() {
        return skin;
    }
    
    
}
