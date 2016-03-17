package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class MainMenuPage extends MenuPage {

    private final MenuManager menuManager;

    public enum Type {
        MENU,
        PAUSED
        
    }
    public MainMenuPage(Skin skin, MenuManager menuManager, Type type) {
        super(skin, "menu_bg");
        this.menuManager=menuManager;
        
        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        
        if(type==Type.MENU) {
            
          addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Spiel starten", new LevelSelectionPage(skin, menuManager));  
        //addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 150, 50, "Start Game", this::startGame);
        }
        else if(type==Type.PAUSED) {
            addLeftAlignedButton(xOffset, yOffset - yStep * (i++), 150, 50, "Fortsetzen", () ->{ menuManager.popPage();Game.pauseGame();},"buttonSound");
        }
        addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Optionen", new MenuOptions(skin, menuManager));
        addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Credits", new MenuPageCredits(skin, menuManager));
    
        if(type==Type.MENU) {
       //addLeftAlignedButton(xOffset, yOffset - yStep *( 2* i++), 100, 50, "Beenden", () -> System.exit(-1),"einhornEmpathy");
        addLeftAlignedButton(xOffset, yOffset - yStep *( 2* i++), 100, 50, "Beenden", this::systemExitDelay,"einhornEmpathy");

        }
        else if (type==Type.PAUSED) {
            addLeftAlignedButton(xOffset, yOffset - yStep*(2* i++), 100, 50, "Menü", this::stopGame,"buttonSound");
         }
        
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
        
        addLeftAlignedButton(x, y, 150, 40, text, () -> menuManager.pushPage(page),"buttonSound");
    }
    
    private void systemExitDelay(){
        
        try {
            Thread.sleep(1300);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.exit(-1);
    }
        
    

   

}
