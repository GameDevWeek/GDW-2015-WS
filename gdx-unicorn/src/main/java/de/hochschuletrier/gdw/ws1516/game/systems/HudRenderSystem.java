package de.hochschuletrier.gdw.ws1516.game.systems;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

public class HudRenderSystem extends IteratingSystem {



    private final AssetManagerX assetManager;
    private final BitmapFont font;
    
    public HudRenderSystem(int priority) {
        super(Family.all(PlayerComponent.class).get(),priority);
        
        assetManager=Main.getInstance().getAssetManager();
        font = assetManager.getFont("verdana_32");

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub
        PositionComponent scoreComp = ComponentMappers.position.get(entity);
        
        Main.getInstance().screenCamera.bind();
      
        String s = "time";
        String score = "x"+scoreComp.x;
        font.draw(DrawUtil.batch, s, 400, 20);
        font.draw(DrawUtil.batch,score, 800,20);
        
        FileHandle handle = Gdx.files.internal("data/ui/menu/heart3.png");
        Texture hearts = new Texture(handle);
        Texture coins = new Texture(handle);
        DrawUtil.draw(hearts, Gdx.graphics.getWidth()-100, Gdx.graphics.getHeight(), 50, 50);
        DrawUtil.draw(coins, 900, 20, 50, 50);
    }
    

}
