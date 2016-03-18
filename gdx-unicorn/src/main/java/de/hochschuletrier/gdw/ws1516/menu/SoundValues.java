package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class SoundValues {
    //public static  Skin skin = ((MainMenuState) Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
//    private final static Skin skin = new Skin(Gdx.files.internal("data/ui/menu/skins/menu.json"));
//    public static Slider generalSlider = new Slider(0, 100, 1, false, skin, "default-horizontal");
//    public static Slider  musicSlider = new Slider(0, 100, 1, false, skin, "default-horizontal");
//    public static Slider soundSlider = new Slider(0, 100, 1, false, skin, "default-horizontal");
    protected static float generalValue;
    protected static float musicValue;
    protected static float soundValue;

    public SoundValues() {
    }

    public static float getGeneralValue() {
        return generalValue;
    }

    public static float getMusicValue() {
        return musicValue;
    }

    public static float getSoundValue() {
        return soundValue;
    }
    public static void setGeneralValue(float x){
        generalValue=x;
    }
    public static void setMusicValue(float x){
        musicValue=x;
    }
    public static void setSoundValue(float x){
        soundValue=x;
    }

}
