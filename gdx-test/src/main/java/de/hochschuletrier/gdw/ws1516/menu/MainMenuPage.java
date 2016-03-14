package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MainMenuPage extends MenuPage {

    public MainMenuPage(Skin skin, String background) {
        super(skin, background);
        
        int i = 0;
        int xOffset = 20;
        int yOffset = 20;
        int yStep = 55;
        
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Start Game", this::dummy);
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Options", this::dummy);
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Credits", this::dummy);
        addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 100, 50, "Exit", this::dummy);
        
        
    }

    private void dummy() {
        System.out.println("Hallo");
    }
    

   

}
