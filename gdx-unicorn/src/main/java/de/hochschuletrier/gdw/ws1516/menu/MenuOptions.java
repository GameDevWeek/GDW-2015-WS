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
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ws1516.game.systems.SoundSystem;

public class MenuOptions extends MenuPage {

    private static float generalSound;
    


    public MenuOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        System.out.println("Test");
        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        
        generalSound = 5;
        addLabeledSlider(SoundSlider.getGeneralSlider(),xOffset, yOffset - yStep * (i++), "Allgemein", true);    
        System.out.println("Test1");
        addLabeledSlider( SoundSlider.getMusicSlider(),xOffset, yOffset - yStep * (i++), "Musik", true); 
        System.out.println("Test2");
        addLabeledSlider(SoundSlider.getSoundSlider(),xOffset, yOffset - yStep * (i++), "Sound", true);
        System.out.println("Test3");
        addLeftAlignedButton(xOffset, yOffset - yStep * (i++), 120, 50, "Zurücksetzen", this::reset, "buttonSound");    
        
        
        SoundSlider.getGeneralSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                generalSound = SoundSlider.getGeneralSlider().getValue() / 10;
                MusicManager.setGlobalVolume(SoundSlider.getMusicSlider().getValue() / 1000 * generalSound);
                       
            }
        });

        SoundSlider.getMusicSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicManager.setGlobalVolume(SoundSlider.getMusicSlider().getValue() / 1000 * generalSound);
                      

            }
        });

        SoundSlider.getSoundSlider().addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                SoundSystem.setGlobalVolume(SoundSlider.getSoundSlider().getValue() / 1000 * generalSound);        
                soundTest();

            }
        });
      
        addLeftAlignedButton(xOffset, 40, 100, 50, "Menü", () -> menuManager.popPage(), "buttonSound");
    }

    public void soundTest() {
        SoundEmitter.playGlobal(assetManager.getSound("einhornEmpathy"), false);
    }

    public void reset() {
        System.out.println("start");;
        SoundSlider.getGeneralSlider().setValue(50);
        SoundSlider.getMusicSlider().setValue(50);
        SoundSlider.getSoundSlider().setValue(50);
        MusicManager.setGlobalVolume(SoundSlider.getMusicSlider().getValue() / 1000 * generalSound);         
        SoundSystem.setGlobalVolume(SoundSlider.getSoundSlider().getValue() / 1000 * generalSound);
        System.out.println("end");
    }
  
}
