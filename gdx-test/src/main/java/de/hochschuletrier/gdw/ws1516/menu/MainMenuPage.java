package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class MainMenuPage extends MenuPage {

    public MainMenuPage(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        
        int i = 0;
        int xOffset = 20;
        int yOffset = 20;
        int yStep = 55;
        
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Start Game", this::dummy);
      
        addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Options", new MenuOptions(skin, menuManager));
        addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Credits", new MenuPageCredits(skin, menuManager));
    
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Exit", this::dummy);
        
        
    }

    private void dummy() {
        System.out.println("Hallo");
    }
    
    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 300, 40, text, () -> menuManager.pushPage(page));
    }
    

   

}
