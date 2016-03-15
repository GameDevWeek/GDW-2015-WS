package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;

public class HudRenderSystem extends IteratingSystem {

//    private final BitmapFont font;
//    private final Game game;
    public HudRenderSystem(int priority) {
        super(Family.all(PlayerComponent.class).get(),priority);
        
       
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void update(float delta) {
        
        
        Main.getInstance().screenCamera.bind();
        
        FileHandle handle = Gdx.files.internal("data/dummies/coin.png");
        Texture texture = new Texture(handle);
        DrawUtil.draw(texture , 10, 10, 100, 100);
        
        
    }

}
