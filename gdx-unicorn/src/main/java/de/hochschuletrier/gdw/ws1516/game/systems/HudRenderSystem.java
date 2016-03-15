package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class HudRenderSystem extends IteratingSystem {


    private final AssetManagerX assetManager;
    
    public HudRenderSystem(int priority) {
        super(Family.all(PlayerComponent.class).get(),priority);
        assetManager=Main.getInstance().getAssetManager();
       
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void update(float delta) {
        
        
        Main.getInstance().screenCamera.bind();
        
       
        
        Skin skin = ((MainMenuState)Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
        Label label = new Label("Hallo Welt", skin);
        
        
        BitmapFont font = assetManager.getFont("quartz_50");
        String s = "Hallo Welt";
        
       
        font.draw(DrawUtil.batch, s, 400, 585);
         FileHandle handle = Gdx.files.internal("data/dummies/coin.png");
        
        Texture hearts = new Texture(handle);
        DrawUtil.draw(hearts, 20, 540, 50, 50);
        
   //     DrawUtil.draw(texture , 10, 10, 100, 100);
        
        
        
        
        
        
        
        
    }

}
