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
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;
import de.hochschuletrier.gdw.ws1516.events.FinalScoreEvent;

public class EndPage extends MenuPage implements FinalScoreEvent.Listener {
    
    public enum Type {
        WIN,
        GAMEOVER
    }
    
    private long finalScore;
    private ScoreComponent scoreComp  = new ScoreComponent();

    public EndPage(Skin skin, MenuManager menuManager, String background, Type type) {
        super(skin, background);
        
        Main.getInstance().screenCamera.bind();
        AssetManagerX assetManager = Main.getInstance().getAssetManager();
        
        String message;
        Label label;
        Texture endImage;
        Texture bonbons = assetManager.getTexture("drop");
        Texture chocoCoins = assetManager.getTexture("coin_hud");
        Sound sound;
        
        Label chocoScore;
        Label bonbonScore;
        
        FinalScoreEvent.register(this);
                
        if(type==Type.GAMEOVER) {
            message="Verloren!";
            label = new Label(message, skin, "gameover");
            endImage = assetManager.getTexture("dead_unicorn_gameover");
            sound = assetManager.getSound("lose_sound");
        }
        else
        {
            message="Gewonnen!";
            label = new Label(message, skin, "win");
            endImage = assetManager.getTexture("drop");
            sound = assetManager.getSound("win_sound");
            DecoImage bonbons_image = new DecoImage(bonbons);
            DecoImage chocoCoins_image = new DecoImage(chocoCoins);
            chocoCoins_image.setPosition(20, 350);
            bonbons_image.setPosition(20, 420);
            chocoScore = new Label("x " + scoreComp.chocoCoins, skin, "default");
            bonbonScore = new Label("x " + scoreComp.bonbons, skin, "default");
            
            chocoScore.setPosition(20+70, 350);
            bonbonScore.setPosition(20+70, 420);
            super.addActor(bonbonScore);
            super.addActor(chocoScore);
            super.addActor(bonbons_image);
            super.addActor(chocoCoins_image);
            
        }
        
        label.setPosition(400, 350);
        SoundEmitter.playGlobal(sound, false);
        
        DecoImage endPicture = new DecoImage(endImage);
        endPicture.setPosition(300, 50);
        addCenteredButton(430, 330, 100, 50, "Nochmal versuchen", this::startGame, "einhornMotivated");
        addCenteredButton(620, 330, 100, 50, "Ins Hauptmenü", this::stopGame, "menu");
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

    @Override
    public void onFinalScoreChanged(long score, ScoreComponent scoreComponent) {
        
        finalScore = score;
        scoreComp=scoreComponent;
        
    }

}
