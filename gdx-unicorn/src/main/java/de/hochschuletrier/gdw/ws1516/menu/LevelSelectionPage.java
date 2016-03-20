package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    private Label level_title;
    private String[] title;
    
    
    public LevelSelectionPage(Skin skin, MenuManager menuManager) {
        super(skin, "menu_bg");
       
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
        
        int x = 55;
        Texture level_preview_texture = assetManager.getTexture(levelNames.get(0));
        Texture buttonBack_texture = assetManager.getTexture("prev_Button");
        Texture buttonNext_texture = assetManager.getTexture("next_Button");


       
        level_title = new Label("",skin, "default");
        level_preview_d = new DecoImage(assetManager.getTexture(levelNames.get(level_preview_index)));
        
        level_previews = new Texture[levelNames.size()];
        title = new String[4];
        title[0] = "1";
        title[1] = "2";
        title[2] = "3";
        title[3] = "4";
        for (String levelName : levelNames) {
            level_previews[level_preview_count] = assetManager.getTexture(levelName);
            level_preview_count++;
        }
        
        
        level_preview_d.setTexture(level_previews[level_preview_index]);
        level_preview_d.setBounds(x, 240, 360, 200);
        level_title.setText(title[level_preview_index]);
        level_title.setPosition(x, 460);

        int right = x + 360;
        createImageButton(buttonBack_texture, right-2*buttonNext_texture.getWidth() - buttonNext_texture.getWidth()/2, 200, 50, 50, this::previousLevel, "buttonSound", true, true);
        createImageButton(buttonNext_texture, right-buttonNext_texture.getWidth(), 200, 50, 50, this::nextLevel, "buttonSound", true, true);
        
        addLeftAlignedButton(x, 191, 100, 50, "Spielen", this::startGame, "einhornMotivated");
        addPageEntry(menuManager, x, 136, "Hilfe", new HelpPage(skin, menuManager));  
        addLeftAlignedButton(x, 40, 100, 50, "Zurück", () -> menuManager.popPage(),"zurueck");
        
     
        
        super.addActor(level_preview_d);
        super.addActor(level_title);
        
        
        
    }
    
    private void setLevel(int index) {
     //   level_preview_d.setTexture(assetManager.getTexture(levelNames.get(index)));
        level_preview_d.setTexture(level_previews[level_preview_index]);
    }
    private void nextLevel() {
        level_preview_index++;
        level_preview_index %= level_preview_count;
        setLevel(level_preview_index);
        level_title.setText(title[level_preview_index]);
        
                    
    }
    
    private void previousLevel() {
        level_preview_index--;
        
        if(level_preview_index==-1) {
            level_preview_index=level_preview_count-1;
        }
        setLevel(level_preview_index);
        setLevel(level_preview_index);
        level_title.setText(title[level_preview_index]);
    }
    
    private void startGame() {
        if (!main.isTransitioning()) {
            final String filename = levelNames.get(level_preview_index);
            if(Gdx.files.internal(filename).exists()) {
                Game game = new Game();
                game.init(assetManager, filename);
                main.changeState(new GameplayState(assetManager, game, assetManager.getMusic(filename)));
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
