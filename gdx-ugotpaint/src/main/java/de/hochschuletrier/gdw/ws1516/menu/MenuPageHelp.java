package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class MenuPageHelp extends MenuPage {

    public MenuPageHelp(Skin skin, MenuManager menuManager) {
        super(skin, false);

        float x = 360;
        float y = 500;
        float i = 0;
        addLabel(x, y - (i++)* LABEL_STEP, "Roter Spieler");
        addLabel(x, y - (i++)* LABEL_STEP, "Bewegung: Pfeiltasten");
        addLabel(x, y - (i++)* LABEL_STEP, "Schuss: m");
        i+=0.5f;
        addLabel(x, y - (i++)* LABEL_STEP, "Blauer Spieler");
        addLabel(x, y - (i++)* LABEL_STEP, "Bewegung: wasd");
        addLabel(x, y - (i++)* LABEL_STEP, "Schuss: q");
        i+=0.5f;
        addLabel(x, y - (i++)* LABEL_STEP, "Screenshot: strg + F1");
        addLeftAlignedButton(MENU_X, MENU_Y - MENU_STEP * 3, BUTTON_WIDTH, BUTTON_HEIGHT, "Zurück", () -> menuManager.popPage());
        addForeground();
    }

    private void addLabel(float x, float y, String text) {
        Label label = new Label(text, skin);
        label.setPosition(x, y);
        addActor(label);
    }

}
