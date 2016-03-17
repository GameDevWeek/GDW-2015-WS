package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
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
        
        if(type==Type.GAMEOVER) {
            message="Verloren!";
            label = new Label(message, skin, "gameover");
            texture = assetManager.getTexture("dead_unicorn_gameover");
        }
        else
        {
            message="Gewonnen!";
            label = new Label(message, skin, "win");
            texture = assetManager.getTexture("trex");
        }
        
        label.setPosition(0.40F*Gdx.graphics.getWidth(), 0.5F*Gdx.graphics.getHeight());
        
        DecoImage endPicture = new DecoImage(texture);
        endPicture.setPosition(500, 30);
        addCenteredButton(450, 250, 100, 50, "Retry", this::startGame, "einhornMotivated");
        addCenteredButton(600, 250, 100, 50, "Menü", this::stopGame, "einhornMotivated");
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
