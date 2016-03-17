package de.hochschuletrier.gdw.ws1516.menu;

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
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;


public class MenuOptions extends MenuPage {
    
    private Sound sound = (Sound)assetManager.getSound("EinhornEmpathy");
    

    public MenuOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
       
        
        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        
        long id = sound.play();
        sound.stop();
        
        Slider generalSlider = addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"General",true);
        generalSlider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicManager.setGlobalVolume(generalSlider.getValue()/100);
               // System.out.print(" "+generalSlider.getValue()/100);
            }
        });

        Slider musicSlider = addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"Music",true);
        musicSlider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //MusicManager.setGlobalVolume(musicSlider.getValue()/100);
            }
        });
        Slider soundSlider = addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"Sound",true);
        soundSlider.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                sound.stop();
                sound.play(soundSlider.getValue()/100);
                sound.setVolume(id, soundSlider.getValue()/100);
                System.out.print(""+id); 
            }
        });
        
        addLeftAlignedButton(xOffset,yOffset-yStep*(i++), 100, 50,"SoundTest", ()->soundTest());
        addLeftAlignedButton(30, 40, 100, 50,"Menu", ()->menuManager.popPage());
       
    }
    public void soundTest(){
        System.out.println("Test");
        sound.play();
    }
    
    
    

  
}
