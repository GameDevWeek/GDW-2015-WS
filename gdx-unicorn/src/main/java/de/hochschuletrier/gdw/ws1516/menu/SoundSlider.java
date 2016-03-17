package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class SoundSlider {
    //public static  Skin skin = ((MainMenuState) Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
    private final static Skin skin = new Skin(Gdx.files.internal("data/ui/menu/skins/menu.json"));
    public static Slider generalSlider = new Slider(0, 100, 1, false, skin, "default-horizontal");
    public static Slider  musicSlider = new Slider(0, 100, 1, false, skin, "default-horizontal");
    public static Slider soundSlider = new Slider(0, 100, 1, false, skin, "default-horizontal");

    public SoundSlider() {
    }

    public static Slider getGeneralSlider() {
        return generalSlider;
    }

    public static Slider getMusicSlider() {
        return musicSlider;
    }

    public static Slider getSoundSlider() {
        return soundSlider;
    }

}
