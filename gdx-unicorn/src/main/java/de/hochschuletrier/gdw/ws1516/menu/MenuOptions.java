package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;


public class MenuOptions extends MenuPage {


    public MenuOptions(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
       
        
        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;
        
       
        Slider generalSlider = addLabeledSlider(0,100,1,xOffset,yOffset-yStep*(i++),"General",true);
        generalSlider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicManager.setGlobalVolume(generalSlider.getValue()/100);
                System.out.print(" "+generalSlider.getValue()/100);
            }
        });
        
        generalSlider.addListener(new ClickListener(){
            
          
            
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.out.print("Down");
           
             return true;
          }

        });
      generalSlider.addListener(new ClickListener(){
      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          System.out.print("Up");
          
          
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
        soundSlider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //MusicManager.setGlobalVolume(soundSlider.getValue()/100);
            }
        });
        addLeftAlignedButton(30, 40, 100, 50,"Menu", ()->menuManager.popPage());
        
    }
    
    
    

  
}
