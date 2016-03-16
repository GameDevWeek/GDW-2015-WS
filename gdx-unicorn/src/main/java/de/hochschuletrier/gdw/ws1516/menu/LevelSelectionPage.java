package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.menu.MainMenuPage.Type;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class LevelSelectionPage extends MenuPage {

    public LevelSelectionPage(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        
        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        
        
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 150, 50, "Start Game", this::startGame);
        
        
        
        Main.getInstance().screenCamera.bind();
        Texture level_preview_texture = new Texture("data/graphics/unicorn_s.png");
        Texture buttonBack_texture = new Texture("data/graphics/blue_gum_s.png");
        Texture buttonNext_texture = new Texture("data/graphics/blue_gum_s.png");
        Texture[] level_previews = new Texture[5];
        addImageButton(level_preview_texture, 450, 250, 50, 50, this::startGame);
        addImageButton(buttonBack_texture, 430, 230, 50, 50, this::startGame);
        addImageButton(buttonNext_texture, 450+level_preview_texture.getWidth(), 230, 50, 50, this::startGame);
        addLeftAlignedButton(xOffset, yOffset - yStep*(i++), 100, 50, "Menu", () -> menuManager.popPage());
        
    }
    
    private void addImageButton(Texture texture, float x, float y, float width, float height, Runnable runnable) {
       ImageButton ib = new ImageButton(new SpriteDrawable(new Sprite(texture)));
       
       ib.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               runnable.run();
           }
       });
       ib.setPosition(x, y);
      // ib.setBounds(x, y, width, height);
       
       
       super.addActor(ib);
    }
    
    private void nextLevel() {
        
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
    
    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        
        addLeftAlignedButton(x, y, 150, 40, text, () -> menuManager.pushPage(page));
    }

}
