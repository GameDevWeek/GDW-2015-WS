package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class MenuOptions extends MenuPage {
    
    private MenuManager menuManager;

    public MenuOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
       
        this.menuManager=menuManager;
        int i = 0;
        int xOffset = 20;
        int yOffset = 370;
        int yStep = 55;
        
       
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Video", this::enterVideoOptions);
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Sound", this::enterSoundOptions);
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50,"Zurück", this::back);
    }
    
    
    private void enterSoundOptions(){
    }
    
    private void enterVideoOptions(){
    }
    private void back(){
       menuManager.popPage();
    }
    
    

    public static void main(String[] args) {
       

    }

}
