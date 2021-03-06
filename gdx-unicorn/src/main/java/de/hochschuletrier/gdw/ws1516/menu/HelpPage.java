package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class HelpPage extends MenuPage {

    public HelpPage(Skin skin, MenuManager menuManager) {
        
        
        super(skin, "menu_bg");
        int xOffset=190;
        int xStep=120;
        int i = 0;
        
        addLabeledTexture("heart3", "Leben",xOffset+xStep*(i++),545,-10,0,40,40);
        addLabeledTexture("coin_hud", "1 Punkt", xOffset+xStep*(i++)-15,545,-15,0,40,40);
        addLabeledTexture("drop", "3 Punkte", xOffset+xStep*(i++)-15, 545,-10,0,62,40);
        addLabeledTexture("gum_hud", "Blue Gum", xOffset+xStep*(i++), 545,-27,0,40,40);
        addLabeledTexture("bubblegum_rainbow", "Rainbow Gum", xOffset+xStep*(i++), 545,-27,0,40,40);
        addLabeledTexture("hunter", "Jäger",xOffset-30,410,20,0,64,64);
        addLabeledTexture("paparazzi", "Paparazzo",xOffset+75,410,5,0,64,64);
        addLabeledTexture("arrow_Keys", "Bewegen",xOffset+400,100,35,0,152,90);
        addLabeledTexture("fly_Key", "Fliegen",xOffset+153,100,-5,0,44,40);
        addLabeledTexture("dash_Key", "Attacke",xOffset+80,100,-6,0,44,40);
        addLabeledTexture("spit_Key", "Spucken",xOffset+5,100,-15,0,44,40);
        addLabeledTexture("jump_Key", "Springen",xOffset+235,100,30,0,129,40);
        addLabeledTexture("checkpoint_checked", "Checkpoint",xOffset+630,450,25,0,128,117);
        addLabeledTexture("death_zone", "Deathzone",xOffset,310,17,0,120,40);
        
       
        addLeftAlignedButton(55, 40, 100, 50, "Zurück", () -> menuManager.popPage(),"zurueck");
    }
    
    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 150, 40, text, () -> menuManager.pushPage(page),"buttonSound");
    }

}
