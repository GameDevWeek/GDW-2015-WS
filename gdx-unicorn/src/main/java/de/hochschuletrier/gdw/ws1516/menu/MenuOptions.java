package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class MenuOptions extends MenuPage {


    public MenuOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
       
        
        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        
       
        
        addSlider(0,100,1,200,200);
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50,"Zurück", ()->menuManager.popPage());
    }
    

    public static void main(String[] args) {
       

    }

}
