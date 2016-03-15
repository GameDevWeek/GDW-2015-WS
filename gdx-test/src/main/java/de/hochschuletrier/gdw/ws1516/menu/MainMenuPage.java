package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class MainMenuPage extends MenuPage {

    
   
    
    public MainMenuPage(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        
        int i = 0;
        int xOffset = 20;
        int yOffset = 370;
        int yStep = 55;
    
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 150, 50, "Start Game", this::startGame);
      
        addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Options", new MenuOptions(skin, menuManager));
        addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Credits", new MenuPageCredits(skin, menuManager));
    
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Exit", () -> System.exit(-1));
        
        
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
        addLeftAlignedButton(x, y, 100, 40, text, () -> menuManager.pushPage(page));
    }
    

   

}
