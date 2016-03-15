package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class HUD extends Group {

    public HUD(Skin skin, String background) {
        
        TextButton button = new TextButton("Test", skin);
        button.setBounds(30, 30, 100, 50);
        addActor(button);
        
    }

}
