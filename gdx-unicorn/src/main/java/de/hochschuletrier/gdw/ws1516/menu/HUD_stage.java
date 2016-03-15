package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;

public class HUD_stage extends Stage {

    private Game game;
    private HUD hud;    
    
    public HUD_stage(Game game, HUD hud) {
      
        this.game=game;
        this.hud=hud;
        hud.setVisible(true);
        this.addActor(hud);
    }
    
    public InputProcessor getInputProcessor() {
        return this;
    }
    
   
}
