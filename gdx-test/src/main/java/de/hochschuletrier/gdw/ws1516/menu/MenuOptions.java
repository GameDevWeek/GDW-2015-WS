package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class MenuOptions extends MenuPage {

    public MenuOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
       
        int x = 200;
        int i = 0;
        int y = 370;
        
        int yStep = 55;
        addLeftAlignedButton(x,y-yStep * (i++),400,50,"Video",this::enterVideoOptions);
        addLeftAlignedButton(x,y-yStep * (i++),400,50,"Audio",this::enterSoundOptions);
        addLeftAlignedButton(50,50 ,200,50,"Audio",this::enterSoundOptions);
    }
    
    
    private void enterSoundOptions(){
    }
    
    private void enterVideoOptions(){
    }
    

    public static void main(String[] args) {
       

    }

}
