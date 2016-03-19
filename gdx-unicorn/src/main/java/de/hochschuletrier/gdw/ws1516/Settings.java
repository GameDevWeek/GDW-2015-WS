package de.hochschuletrier.gdw.ws1516;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;

import de.hochschuletrier.gdw.commons.gdx.settings.FloatSetting;
import de.hochschuletrier.gdw.commons.gdx.settings.Setting;
import de.hochschuletrier.gdw.commons.gdx.settings.SettingsUtil;
import de.hochschuletrier.gdw.commons.gdx.settings.SettingListener;
import de.hochschuletrier.gdw.ws1516.game.systems.SoundSystem;

public class Settings {

    private static final Preferences prefs = Gdx.app.getPreferences(Settings.class.getName() + ".xml");
    public static final FloatSetting generalValue = new FloatSetting(prefs, "general_Value", 50);
    public static final FloatSetting musicValue = new FloatSetting(prefs, "music_Value", 50);
    public static final FloatSetting soundValue = new FloatSetting(prefs, "sound_Value", 50);

    public static void flush() {
        prefs.flush();
    }

    public static void reset() {
        SettingsUtil.reset(Settings.class);
    }

    private static void setVolumes() {
        System.out.println(""+Settings.generalValue.get()+" "+Settings.musicValue.get()+""+Settings.soundValue.get());
        final float music = musicValue.get() / 100;
        final float sound = soundValue.get() / 100;
        final float general = generalValue.get() / 100;
        MusicManager.setGlobalVolume(music * general);
        SoundEmitter.setGlobalVolume(sound * general);
    }

    static void init() {
        SettingListener<Float> volumeListener = (Setting s, Float v)->setVolumes();
        generalValue.addListener(volumeListener);
        musicValue.addListener(volumeListener);
        soundValue.addListener(volumeListener);
        setVolumes();
    }
}
