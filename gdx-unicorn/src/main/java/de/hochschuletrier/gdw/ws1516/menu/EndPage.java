package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class EndPage extends MenuPage {
    
    public enum Type {
        WIN,
        GAMEOVER
    }

    public EndPage(Skin skin, MenuManager menuManager, String background, Type type) {
        super(skin, background);
        
        Main.getInstance().screenCamera.bind();
        AssetManagerX assetManager = Main.getInstance().getAssetManager();
              
        String message;
        Label label;
        Texture texture;
        Sound sound;
        
        if(type==Type.GAMEOVER) {
            message="Verloren!";
            label = new Label(message, skin, "gameover");
            texture = assetManager.getTexture("dead_unicorn_gameover");
            sound = assetManager.getSound("lose_sound");
        }
        else
        {
            message="Gewonnen!";
            label = new Label(message, skin, "win");
            texture = assetManager.getTexture("trex");
            sound = assetManager.getSound("win_sound");
        }
        
        label.setPosition(400, 300);
        SoundEmitter.playGlobal(sound, false);
        
        DecoImage endPicture = new DecoImage(texture);
        endPicture.setPosition(500, 30);
        addCenteredButton(450, 250, 100, 50, "Nochmal versuchen", this::startGame, "einhornMotivated");
        addCenteredButton(600, 250, 100, 50, "Ins Hauptmenü", this::stopGame, "einhornMotivated");
        super.addActor(label);
        super.addActor(endPicture);
        
        
    }
    
    private void startGame() {
        if (!main.isTransitioning()) {      
            Game game = new Game();
            game.init(assetManager);
            main.changeState(new GameplayState(assetManager, game), new SplitHorizontalTransition(500), null);
            
        }
    }
    
    private void stopGame() {
        if (!main.isTransitioning()) {
            main.changeState(main.getPersistentState(MainMenuState.class));
            
        }
        
    }

}
