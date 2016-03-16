package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class MenuOptions extends MenuPage {


    public MenuOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
       
        
        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        
       
        Slider slider = addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"General",true);
        slider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                
            }
        });
        addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"Sound",true);
        addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"Music",true );
        
        addLeftAlignedButton(30, 40, 100, 50,"Menu", ()->menuManager.popPage());
    }
    
    
    

  
}
