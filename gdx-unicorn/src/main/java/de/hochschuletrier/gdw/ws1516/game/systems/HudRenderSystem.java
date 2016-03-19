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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.CircularProgressRenderer;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;
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
        Texture coin = assetManager.getTexture("coin_hud");
        Texture blue_gum = assetManager.getTexture("gum_hud");
        Texture hornAttackDummy;
        Texture clock = assetManager.getTexture("clock_hud");
        
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
        
        if(playerComp.hornAttackCooldown==0) {
            hornAttackDummy = assetManager.getTexture("dash_Cooldown");
        }
        else {
            hornAttackDummy = assetManager.getTexture("dash_Cooldown");
        }
 
     
        
        
        
        
        
        float heart_x =  20;
        float heart_y = 20;
        
        float lives_x = heart_x+55;
        float lives_y = heart_y+10;
        
       
        
        float gum_x = lives_x + 60;
        float gum_y = heart_y;
        
        float gum_count_x = gum_x + 55;
        float gum_count_y = heart_y+10;
        
        
        float time_x = displayWidth-120;
        float time_y = heart_y;
        
        float clock_x = time_x +70;
        float clock_y = time_y-12;
        
        float coin_x = clock_x;
        float coin_y = time_y+35;
        
        float score_x = time_x;
        float score_y = time_y+45;
        
        float hornAttackDummy_x = 60;
        float hornAttackDummy_y = displayHeight - 60;
        
        int minutes_int = (int) scoreComp.playedSeconds/60;
        String minutes_string = String.valueOf(minutes_int);
        int seconds_int = (int) scoreComp.playedSeconds%60;
        String seconds_string = String.valueOf(seconds_int);

        if(seconds_string.length()==1) {
            seconds_string = "0"+seconds_string;
        }
        
        int lives = playerComp.lives;
        int gum_count_int = playerComp.blueGumStacks;
           
        String lives_string = "x " + String.valueOf(lives);
        String gum_count_string = "x " + String.valueOf(gum_count_int); 
        String time = minutes_string+":"+seconds_string;
        String score = String.valueOf(3*scoreComp.bonbons+scoreComp.chocoCoins);
        
        
                       
        DrawUtil.draw(heart, heart_x, heart_y, 40, 40);
        font.draw(DrawUtil.batch, lives_string, lives_x, lives_y);
        DrawUtil.draw(blue_gum, gum_x, gum_y, 40, 40);
        font.draw(DrawUtil.batch, gum_count_string, gum_count_x, gum_count_y);
        font.draw(DrawUtil.batch, time, time_x, time_y);
        DrawUtil.draw(coin, coin_x, coin_y, 40, 40);
        font.draw(DrawUtil.batch,score, score_x, score_y);
        //DrawUtil.draw(hornAttackDummy, hornAttackDummy_x, hornAttackDummy_y, 92, 92);
        DrawUtil.draw(clock, clock_x, clock_y, 40,40);
        
        CircularProgressRenderer dashCooldownRenderer = new CircularProgressRenderer(hornAttackDummy);
        float cooldown;
        if (playerComp.hornAttackCooldown != 0.0f && playerComp.state != State.HORNATTACK) {
            cooldown = -(playerComp.hornAttackCooldown / GameConstants.HORN_MODE_COOLDOWN);
        } else {
            cooldown = 1.0f;
        }
        dashCooldownRenderer.draw(DrawUtil.batch, hornAttackDummy_x, hornAttackDummy_y, 92, 92, cooldown * 360.0f); 
              
    }

    @Override
    public void onFinalScoreChanged(long score,ScoreComponent scoreComp) {
        /**
         * @author philipp -> gamelogic
         * scorecomp hat alle werte
         */
        finalScore = score;
    }
    

}
