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

    private int level_preview_index=0;
    private int level_preview_count=4;
    private ImageButton level_preview;
    private Texture[] level_previews;
    
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
        level_previews = new Texture[level_preview_count];
      
        level_previews[0] = new Texture("data/graphics/unicorn_s.png");
        level_previews[1] = new Texture("data/graphics/enemy_hunter_s.png");
        level_previews[2] = new Texture("data/graphics/enemy_paparazzi_s.png");
        level_previews[3] = new Texture("data/graphics/bubble_s.png");
        
        level_preview = createImageButton(level_previews[level_preview_index], 450, 250, 50, 50, this::startGame, true);
                    
        createImageButton(buttonBack_texture, 430, 230, 50, 50, this::previousLevel, true);
        createImageButton(buttonNext_texture, 450+level_preview_texture.getWidth(), 230, 50, 50, this::nextLevel, true);
        addLeftAlignedButton(xOffset, yOffset - yStep*(i++), 100, 50, "Menu", () -> menuManager.popPage());
        
    }
    
    private void setLevel(int index) {
        level_preview.remove();
        level_preview = createImageButton(level_previews[index], 450, 250, 50, 50, this::startGame, true);
    }
    private void nextLevel() {
        level_preview.remove();
        level_preview_index++;
        level_preview_index %= level_preview_count;
        setLevel(level_preview_index);
        
        
    
    }
    
    private void previousLevel() {
        level_preview.remove();
        level_preview_index--;
        
        if(level_preview_index==-1) {
            level_preview_index=level_preview_count-1;
        }
        setLevel(level_preview_index);
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
