package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.TimeUtils;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;



public class EffectsRenderSystem extends IteratingSystem {
    
    private long startTime;
    
    private float paparazziDurationLeft;
    private float paparazziStartDuration;
    
    private Texture effectsOverlay;
    
    private final ConsoleCmd paparazzi = new ConsoleCmd("pap", 0, "Usage: paparazzi [duration] - Activates paparazzi mode for [duration] seconds") { @Override public void execute(List<String> args) { startPaparazzi(args); } };
    private final CVarFloat paparazziIntensity = new CVarFloat("paparazziIntensity", GameConstants.PAPARAZZI_INTENSITY, 0.0f, Float.MAX_VALUE, 0, "");
    private final CVarFloat paparazziAlpha = new CVarFloat("paparazziAlpha", GameConstants.PAPARAZZI_ALPHA, 0.0f, Float.MAX_VALUE, 0, "");

    @SuppressWarnings("unchecked")
    public EffectsRenderSystem(int priority) {
        super(Family.all().get(), priority);
        
        // DEBUG
        this.effectsOverlay = new Texture(Gdx.files.getFileHandle("data/graphics/trex.png", FileType.Local));
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
    
    @Override
    public void addedToEngine(Engine engine) {

        //DEBUG
        Main.getInstance().console.register(paparazzi);
        Main.getInstance().console.register(paparazziIntensity);
        Main.getInstance().console.register(paparazziAlpha);
        
        super.addedToEngine(engine);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        
        //DEBUG
        Main.getInstance().console.unregister(paparazzi);
        Main.getInstance().console.unregister(paparazziIntensity);
        Main.getInstance().console.unregister(paparazziAlpha);
        
        super.removedFromEngine(engine);
    }
    
    @Override
    public void update(float deltaTime) {
        
        if (paparazziDurationLeft > 0.0f) {
                    
            paparazziDurationLeft -= deltaTime;
            ShaderProgram shader = ShaderLoader.getPaparazziShader();
            DrawUtil.setShader(shader);
            if(shader != null)
            {
                float[] dimensions = new float[]{ Gdx.graphics.getWidth(), Gdx.graphics.getHeight() };
                shader.setUniform2fv("u_frameDimension", dimensions, 0, 2);
                shader.setUniformf("u_startDuration", paparazziStartDuration);
                shader.setUniformf("u_durationLeft", paparazziDurationLeft);
                shader.setUniformf("u_paparazziAlpha", paparazziAlpha.get());
                shader.setUniformf("u_paparazziIntensity", paparazziIntensity.get());
                shader.setUniformf("u_time", (float)TimeUtils.timeSinceMillis(startTime) * 0.001f);
            }
            
            DrawUtil.batch.draw(effectsOverlay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            DrawUtil.batch.flush();
        }
        
        DrawUtil.setShader(null);
        
        super.update(deltaTime);
    }

    protected void startPaparazzi(List<String> args) {
        if(args.size() <= 1)
        {
            paparazziStartDuration = 5.0f;
        }
        else
        {
            try
            {
                paparazziStartDuration = Float.parseFloat(args.get(1));
            }
            catch(Exception e)
            {
                paparazziStartDuration = 5.0f;
            }
        }
        paparazziDurationLeft = paparazziStartDuration;
        startTime = TimeUtils.millis();
    }

}
