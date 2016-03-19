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
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;
import de.hochschuletrier.gdw.ws1516.events.FinalScoreEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRestartEvent;

public class EndPage extends MenuPage {
    
    public enum Type {
        WIN,
        GAMEOVER
    }
    
    private long finalScore;
    

    public EndPage(Skin skin, MenuManager menuManager, String background, Type type, ScoreComponent scoreComp) {
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
           // endImage = assetManager.getTexture("drop");
            sound = assetManager.getSound("win_sound");
            DecoImage bonbons_image = new DecoImage(bonbons);
            DecoImage chocoCoins_image = new DecoImage(chocoCoins);
            chocoScore = new Label("    x " + scoreComp.chocoCoins + " (1 Punkt) =                       " + scoreComp.chocoCoins, skin, "default");
            
            bonbonScore = new Label("    x " + scoreComp.bonbons + " (3 Punkte) =                     " + scoreComp.bonbons*3, skin, "default");
            Label trennstrich = new Label("___________________________________________________", skin, "default");
            
            long timeScore = (long) (GameConstants.SCORE_TIME_POINTS - scoreComp.playedSeconds);
            //Label timeScore_label = new Label(""+timeScore + " (" + String.valueOf((int) (GameConstants.SCORE_TIME_POINTS))+" Abzug pro Sekunde)", skin, "default");
            Label timeScore_label = new Label("Zeitbonus              =                     "+ timeScore , skin, "default");
            
            Label finalScore = new Label(String.valueOf(scoreComp.bonbons*3+scoreComp.chocoCoins+timeScore), skin, "win");
            
            chocoCoins_image.setPosition(200, 600);
     
            chocoScore.setPosition(200+70, 600);
            bonbons_image.setPosition(200, 550);
            bonbonScore.setPosition(200+70, 550);
            timeScore_label.setPosition(200, 450);
            trennstrich.setPosition(180, 400);
            finalScore.setPosition(300, 350);
            super.addActor(bonbonScore);
            super.addActor(chocoScore);
            super.addActor(bonbons_image);
            super.addActor(chocoCoins_image);
            super.addActor(trennstrich);
            super.addActor(finalScore);
            super.addActor(timeScore_label);
            
            
        }
        
        label.setPosition(200, 600);
        SoundEmitter.playGlobal(sound, false);
        
        //DecoImage endPicture = new DecoImage(endImage);
       //endPicture.setPosition(300, 50);
       
                
       addCenteredButton(200, 200, 100, 50, "Nochmal versuchen", this::startGame, "einhornMotivated");
        addCenteredButton(500, 200, 100, 50, "Ins Hauptmenü", this::stopGame, "menu");
       super.addActor(label);
       //super.addActor(endPicture);
        
        
    }

    private void startGame() {
        GameRestartEvent.emit();
    }
    
    private void stopGame() {
        if (!main.isTransitioning()) {
            main.changeState(main.getPersistentState(MainMenuState.class));
            
        }  
    }
}
