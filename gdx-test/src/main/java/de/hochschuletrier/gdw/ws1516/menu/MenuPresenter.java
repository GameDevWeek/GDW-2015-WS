package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class MenuPresenter {

    private MenuOptions options;
    private MainMenuPage mainMenu;
    private MenuPageCredits credits;
    private Skin skin;
    
    public MenuPresenter(Skin skin, MenuManager menuManager) {
        options = new MenuOptions(skin, menuManager);
        mainMenu = new MainMenuPage(skin, menuManager);
        credits = new MenuPageCredits(skin, menuManager);
    }
}
