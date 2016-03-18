package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import de.hochschuletrier.gdw.commons.gdx.settings.FloatSetting;
import de.hochschuletrier.gdw.commons.gdx.settings.SettingsUtil;

public class Settings {
    private static final Preferences prefs = Gdx.app.getPreferences(Settings.class.getName()+".xml");
    public static final FloatSetting generalValue = new FloatSetting(prefs,"general_Value",50);
    public static final FloatSetting musicValue = new FloatSetting(prefs,"music_Value",50);
    public static final FloatSetting soundValue = new FloatSetting(prefs,"sound_Value",50);
    
    public static void reset(){
        SettingsUtil.reset(Settings.class);
    }
}

