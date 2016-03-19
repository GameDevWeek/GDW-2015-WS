package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.menu.MainMenuPage.Type;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LevelSelectionPage extends MenuPage {

    private int level_preview_index=0;
    private int level_preview_count=0;
    private ImageButton level_preview;
    private DecoImage level_preview_d;
    private final ArrayList<String> levelNames = new ArrayList();
    private Texture[] level_previews;
    
    
    public LevelSelectionPage(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
        int xOffset=310;
        int xStep=120;
        int i = 0;
        Main.getInstance().screenCamera.bind();
        
        try {
            Map<String, String> images = JacksonReader.readMap("data/json/images.json", String.class);
            
            for (String key : images.keySet()) {
                if(key.endsWith(".tmx")) {
                    levelNames.add(key);
                }
            }
            Collections.sort(levelNames);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Texture level_preview_texture = assetManager.getTexture(levelNames.get(0));
        Texture buttonBack_texture = assetManager.getTexture("prev_Button");
        Texture buttonNext_texture = assetManager.getTexture("next_Button");
        
        level_preview_d = new DecoImage(assetManager.getTexture(levelNames.get(level_preview_index)));
        
        level_previews = new Texture[levelNames.size()];
        for (String levelName : levelNames) {
            level_previews[level_preview_count] = assetManager.getTexture(levelName);
            level_preview_count++;
        }
      
        level_preview_d.setTexture(level_previews[level_preview_index]);
        level_preview_d.setPosition(312, 240);
                      
        createImageButton(buttonBack_texture, 310-20-buttonBack_texture.getWidth(), 260, 50, 50, this::previousLevel, "buttonSound", true, true);
        createImageButton(buttonNext_texture, 310+level_preview_texture.getWidth()+20, 260, 50, 50, this::nextLevel, "buttonSound", true, true);
        
        addCenteredButton(512, 200, 50, 50, "Spielen", this::startGame, "einhornMotivated");
        addLeftAlignedButton(55, 40, 100, 50, "Zurück", () -> menuManager.popPage(),"zurueck");
        addLabeledTexture("heart3", "Leben",xOffset+xStep*(i++),550,-10,0,40,40);
        addLabeledTexture("coin_hud", "1 Punkt", xOffset+xStep*(i++)-15,550,-15,0,40,40);
        addLabeledTexture("drop", "3 Punkte", xOffset+xStep*(i++), 550,-15,0,62,40);
        addLabeledTexture("gum_hud", "Kaugummi", xOffset+xStep*(i++), 550,-27,0,40,40);
        addLabeledTexture("hunter", "Jäger",xOffset+xStep*(i++),450,20,0,64,64);
        addLabeledTexture("paparazzi", "Paparazzi",xOffset+xStep*(i++),450,20,0,64,64);
        addLabeledTexture("arrow_Keys", "Bewegen",xOffset+350,50,35,0,152,90);
        addLabeledTexture("dash_Key", "Paparazzi",xOffset,200,5,0,44,40);
        addLabeledTexture("fly_Key", "Paparazzi",xOffset,300,5,0,44,40);
        addLabeledTexture("spit_Key", "Paparazzi",xOffset,400,5,0,44,40);
        addLabeledTexture("jump_Key", "Springen",xOffset+150,50,60,0,129,40);
        
        super.addActor(level_preview_d);
        
        
        
    }
    
    private void setLevel(int index) {
     //   level_preview_d.setTexture(assetManager.getTexture(levelNames.get(index)));
        level_preview_d.setTexture(level_previews[level_preview_index]);
    }
    private void nextLevel() {
        level_preview_index++;
        level_preview_index %= level_preview_count;
        setLevel(level_preview_index);
                    
    }
    
    private void previousLevel() {
        level_preview_index--;
        
        if(level_preview_index==-1) {
            level_preview_index=level_preview_count-1;
        }
        setLevel(level_preview_index);
    }
    
    private void startGame() {
        if (!main.isTransitioning()) {
            final String filename = levelNames.get(level_preview_index);
            if(Gdx.files.internal(filename).exists()) {
                Game game = new Game();
                game.init(assetManager, filename);
                main.changeState(new GameplayState(assetManager, game));
            } else {
                assetManager.getSound("death").play();
            }  
        }
    }
    
       
    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 150, 40, text, () -> menuManager.pushPage(page),"buttonSound");
    }
    
    public int getIndexOfSelectedLevel() {
        return level_preview_index;
    }
}
