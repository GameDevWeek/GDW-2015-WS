package de.hochschuletrier.gdw.ws1516.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;

public class LoadGameState extends BaseGameState {

    private boolean isDone;
    private final AssetManagerX assetManager;
    private final Runnable completeFunc;
    private final Color color = Color.valueOf("73BE28");
    private final Texture loading = new Texture(Gdx.files.internal("data/images/loading.png"));
    private final Texture paintbrush = new Texture(Gdx.files.internal("data/images/paintbrush.png"));

    public LoadGameState(AssetManagerX assetManager, Runnable completeFunc) {
        this.assetManager = assetManager;
        this.completeFunc = completeFunc;
    }

    public void render() {
        Main.getInstance().screenCamera.bind();

        DrawUtil.draw(loading, 0, 0);
        float leftDist = 70;
        float rightDist = 70;
        float barHeight = 105;
        float drawWidth = Gdx.graphics.getWidth() - (leftDist + rightDist);
        final int progressWidth = (int) (drawWidth * assetManager.getProgress());
        final float y = 245;
        DrawUtil.fillRect(leftDist, y, progressWidth, barHeight, color);
        DrawUtil.draw(paintbrush, leftDist + progressWidth - 70, y - 115);
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

        render();
    }

    @Override
    public void dispose() {
    }
}
