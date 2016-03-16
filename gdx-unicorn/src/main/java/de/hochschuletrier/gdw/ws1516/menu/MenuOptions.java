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
        
       
        
        addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"Sound");
        addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"Music");
      
        addLeftAlignedButton(30, 40, 100, 50,"Menu", ()->menuManager.popPage());
    }
    

  
}
