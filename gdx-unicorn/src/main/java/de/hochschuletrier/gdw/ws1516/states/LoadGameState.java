package de.hochschuletrier.gdw.ws1516.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;

public class LoadGameState extends BaseGameState {

    private boolean isDone;
    private final AssetManagerX assetManager;
    private final Runnable completeFunc;
    private final Skin skin = new Skin(Gdx.files.internal("data/ui/menu/skins/menu.json"));
    private final Texture loadingTexture = new Texture(Gdx.files.internal("data/graphics/unicorn_s.png"));
    private final Texture rectTexture = new Texture(Gdx.files.internal("data/ui/menu/loading-rect.png"));
    private final Texture background = new Texture(Gdx.files.internal("data/ui/menu/menu_splash.png"));
    
    private final AnimationExtended anim;
    private float stateTime;

    public LoadGameState(AssetManagerX assetManager, Runnable completeFunc) {
        this.assetManager = assetManager;
        this.completeFunc = completeFunc;
        anim=animateUnicorn();
    }
    
    public AnimationExtended animateUnicorn() {
        Texture texture = new Texture(Gdx.files.internal("data/graphics/spritesheets/unicorn_running_rainbow_spritesheet.png"));
        int tileWidth = texture.getWidth() / 8;
        int tileHeight = texture.getHeight() / 1;
        TextureRegion[][] tmp = TextureRegion.split(texture, tileWidth, tileHeight);
        int frameCount = Math.min(8, 8 * 1);
        TextureRegion[] frames = new TextureRegion[frameCount];
        int index = 0;
        for (int i = 0; i < 1 && index < frameCount; i++) {
            for (int j =0; j < 8 && index < frameCount; j++) {
                frames[index] = tmp[i][j];
                frames[index].flip(false, false);
                index++;
            }
        }
        float[] frameDurations = {0.1f};
        AnimationExtended anim = new AnimationExtended(PlayMode.LOOP,
                frameDurations, frames);
        return anim;
    }

    public void render() {
        Main.getInstance().screenCamera.bind();
        
       
        
       // DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.PURPLE);
        DrawUtil.draw(background, 0, 0);
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new Color(0,0,0,0.5F));
        float drawWidth = Gdx.graphics.getWidth() - 100.0f;
     
        DrawUtil.draw(rectTexture, 50, Gdx.graphics.getHeight() / 2 - 25, (int) (drawWidth * assetManager.getProgress()+25), 50);

        TextureRegion keyFrame = anim.getKeyFrame(stateTime);
        DrawUtil.batch.draw(keyFrame, (int) (drawWidth * assetManager.getProgress()), Gdx.graphics.getHeight() / 2 - 25, 0,0, keyFrame.getRegionWidth(), keyFrame.getRegionHeight(), 1, 1, 0);
   
    }

    @Override
    public void update(float delta) {
        if (!isDone) {
            assetManager.update();

            if (assetManager.getProgress() == 1) {
                completeFunc.run();
                isDone = true;
            }
        }
        stateTime += delta;
        render();
    }

    @Override
    public void dispose() {
    }
}
