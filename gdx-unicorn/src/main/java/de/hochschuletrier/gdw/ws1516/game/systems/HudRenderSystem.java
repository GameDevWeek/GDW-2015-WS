package de.hochschuletrier.gdw.ws1516.game.systems;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;
import de.hochschuletrier.gdw.ws1516.events.FinalScoreEvent;

public class HudRenderSystem extends IteratingSystem implements FinalScoreEvent.Listener {



    private final AssetManagerX assetManager;
    private final BitmapFont font;
    private long finalScore;
    
    public HudRenderSystem(int priority) {
        super(Family.all(PlayerComponent.class).get(),priority);
        
        assetManager=Main.getInstance().getAssetManager();
        font = assetManager.getFont("verdana_32");

    }

    @Override
    public void addedToEngine(Engine engine) {
        // TODO Auto-generated method stub
        super.addedToEngine(engine);
        FinalScoreEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        FinalScoreEvent.unregister(this);
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub
        ScoreComponent scoreComp = ComponentMappers.score.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        
        Main.getInstance().screenCamera.bind();
        
        int displayWidth = Gdx.graphics.getWidth();
        int displayHeight = Gdx.graphics.getHeight();
        
        Texture heart;
        Texture cookie = assetManager.getTexture("cookie");
        
        if(playerComp.hitpoints==3) {
            heart = assetManager.getTexture("heart3");
        }
        else if(playerComp.hitpoints==2) {
            heart = assetManager.getTexture("heart2");
        }
        else if(playerComp.hitpoints==1) {
            heart = assetManager.getTexture("heart1");
        }
        else  {
            heart = assetManager.getTexture("heart0");
        }
        
        
        
        float heart_x =  20;
        float heart_y = 20;
        
        float time_x = 0.5F * displayWidth;
        float time_y = 20;
        
        float cookie_x = displayWidth-100;
        float cookie_y = 20;
        
        float score_x = cookie_x + 30;
        float score_y = 20;
        
        
              
        int minutes_int = (int) scoreComp.playedSeconds/60;
        String minutes_string = String.valueOf(minutes_int);
        int seconds_int = (int) scoreComp.playedSeconds%60;
        String seconds_string = String.valueOf(seconds_int);

        if(seconds_string.length()==1) {
            seconds_string = "0"+seconds_string;
        }
                  
        String time = minutes_string+":"+seconds_string;
        String score = String.valueOf(finalScore);
        
                       
       
        
        font.draw(DrawUtil.batch, time, time_x, time_y);
        font.draw(DrawUtil.batch,score, score_x, score_y);
        DrawUtil.draw(heart, heart_x, heart_y, 50, 50);
        DrawUtil.draw(cookie, cookie_x, cookie_y, 50, 50);
    }

    @Override
    public void onFinalScoreChanged(long score) {
        finalScore = score;
    }
    

}
