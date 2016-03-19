package de.hochschuletrier.gdw.ws1516.menu;

import de.hochschuletrier.gdw.ws1516.Settings;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class MenuPageOptions extends MenuPage {

    private Slider generalSlider;
    private Slider musicSlider;
    private Slider soundSlider;

    public MenuPageOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        System.out.println("Test");
        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        
        generalSlider = addLabeledSlider(0,100,1,xOffset, yOffset - yStep * (i++), "Allgemein", true,Settings.generalValue.get());    
        System.out.println("Test1");
        musicSlider = addLabeledSlider( 0,100,1,xOffset, yOffset - yStep * (i++), "Musik", true,Settings.musicValue.get()); 
        System.out.println("Test2");
        soundSlider=addLabeledSlider(0,100,1,xOffset, yOffset - yStep * (i++), "Sound", true,Settings.soundValue.get());
        System.out.println("Test3");
        addLeftAlignedButton(xOffset, yOffset - yStep * (i++), 120, 50, "Zurücksetzen", this::reset, "buttonSound");  
        addPageEntry(menuManager, xOffset, yOffset - yStep*(i++), "Informationen", new HelpPage(skin, menuManager));  
        
        generalSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.generalValue.set(generalSlider.getValue());
                Settings.flush();
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.musicValue.set(musicSlider.getValue());
                Settings.flush();
            }
        });

        soundSlider.addListener(new ClickListener() {
            
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                Settings.soundValue.set(soundSlider.getValue());
                Settings.flush();
                soundTest();
            }
        });
      
        addLeftAlignedButton(xOffset, 40, 100, 50, "Zurück", () -> menuManager.popPage(), "zurueck");
    }

    public void soundTest() {
        SoundEmitter.playGlobal(assetManager.getSound("einhornEmpathy"), false);
    }
    
    public void reset(){
        System.out.println(""+Settings.generalValue.get()+" "+Settings.musicValue.get()+""+Settings.soundValue.get());
        Settings.reset();
        setValuesFromSettings();
    }
    
    public void setValuesFromSettings(){
        generalSlider.setValue(Settings.generalValue.get());
        musicSlider.setValue(Settings.musicValue.get());
        soundSlider.setValue(Settings.soundValue.get());
        System.out.println(""+Settings.generalValue.get()+" "+Settings.musicValue.get()+""+Settings.soundValue.get());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(visible) {
            setValuesFromSettings();
        }
    }
    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 150, 40, text, () -> menuManager.pushPage(page),"buttonSound");
    }

}
