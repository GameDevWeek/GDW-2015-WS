package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;

/**
 * Created by glumbatsch on 20.03.2016.
 */
public class MenuPageWin extends MenuPage {

    public MenuPageWin(Skin skin, boolean drawPainted, String player) {
        super(skin, drawPainted);


        float x = 512;
        float y = 300;

        addLabel(x,y,player + " HAT GEWONNEN!");
        addForeground();

    }


    private void addLabel(float x, float y, String text) {
        Label label = new Label(text, skin);
        label.setPosition(x, y);
        addActor(label);
    }
}
