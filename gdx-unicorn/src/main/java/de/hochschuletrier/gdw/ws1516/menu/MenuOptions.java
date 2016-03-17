package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.audio.Wav.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.SoundEmitterComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.SoundSystem;

public class MenuOptions extends MenuPage {

    private float generalSound;
    private Slider generalSlider;
    private Slider musicSlider;
    private Slider soundSlider;

    public MenuOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");

        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        generalSound = 5;
        generalSlider = addLabeledSlider(0, 100, 1, xOffset, yOffset - yStep
                * (i++), "Allgemein", true);
        musicSlider = addLabeledSlider(0, 100, 1, xOffset, yOffset - yStep
                * (i++), "Musik", true);
        soundSlider = addLabeledSlider(0, 100, 1, xOffset, yOffset - yStep
                * (i++), "Sound", true);
        addLeftAlignedButton(xOffset, yOffset - yStep * (i++), 100, 50, "Zurücksetzen",
                this::reset);
        generalSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                generalSound = generalSlider.getValue() / 10;
                MusicManager.setGlobalVolume(musicSlider.getValue() / 1000
                        * generalSound);
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicManager.setGlobalVolume(musicSlider.getValue() / 1000
                        * generalSound);

            }
        });

        soundSlider.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y,
                    int pointer, int button) {
                SoundSystem.setGlobalVolume(soundSlider.getValue() / 1000
                        * generalSound);
                soundTest();

            }
        });

        addLeftAlignedButton(xOffset, 40, 100, 50, "Menu",
                () -> menuManager.popPage());
    }

    public void soundTest() {
        SoundEmitter.playGlobal(assetManager.getSound("einhornempathy"), false);
    }

    public void reset() {
        generalSlider.setValue(50);
        musicSlider.setValue(50);
        soundSlider.setValue(50);
        MusicManager.setGlobalVolume(musicSlider.getValue() / 1000
                * generalSound);
        SoundSystem.setGlobalVolume(soundSlider.getValue() / 1000
                * generalSound);
        
    }
}
