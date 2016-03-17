package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.CameraTargetComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;



public class EffectsRenderSystem extends IteratingSystem {
    
    private long startTime;
    
    private float paparazziDurationLeft;
    private float paparazziStartDuration;
    
    private Vector2 cameraTargetScreenPos;
    
    private Texture screenOverlayWhiteRect;
    
    private final ConsoleCmd paparazzi = new ConsoleCmd("pap", 0, "Usage: pap (duration) - Activates paparazzi mode for (duration) seconds") { @Override public void execute(List<String> args) { startPaparazzi(args); } };
    private final CVarFloat paparazziIntensity = new CVarFloat("paparazziIntensity", 1.0f, 0.0f, Float.MAX_VALUE, 0, "");
    private float currentPaparazziSeed;

    @SuppressWarnings("unchecked")
    public EffectsRenderSystem(int priority) {
        super(Family.all(CameraTargetComponent.class, PositionComponent.class).get(), priority);
        
        cameraTargetScreenPos = new Vector2( (float) (Gdx.graphics.getWidth()* 0.5), (float) (Gdx.graphics.getHeight() * 0.5) );
        
        // Create white Rectangle Texture to initiate draw call
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.screenOverlayWhiteRect = new Texture(pixmap);
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // save camera target position to set paparazzi effect save circle around player        
        PositionComponent targetPos = ComponentMappers.position.get(entity);
        cameraTargetScreenPos = CameraSystem.worldToScreenCoordinates(targetPos.x, targetPos.y);
        //System.out.println("screenX: " + cameraTargetScreenPos.x + " (worldX: " + targetPos.x + ") # screenY: " + cameraTargetScreenPos.y + " worldY: " + targetPos.y);
    }
    
    @Override
    public void addedToEngine(Engine engine) {

        //DEBUG
        Main.getInstance().console.register(paparazzi);
        Main.getInstance().console.register(paparazziIntensity);
        
        super.addedToEngine(engine);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        
        //DEBUG
        Main.getInstance().console.unregister(paparazzi);
        Main.getInstance().console.unregister(paparazziIntensity);
        
        super.removedFromEngine(engine);
    }
    
    private Vector2 cameraScreenToShaderScreenCoords(Vector2 in)
    {
        return new Vector2(in.x, Gdx.graphics.getHeight() - in.y);
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
                
                shader.setUniformf("u_effectDuration", paparazziStartDuration);
                shader.setUniformf("u_remainingEffectDuration", paparazziDurationLeft);
                
                float[] paparazziColor = new float[]{ 0.0f, 0.0f, 1.0f, 1.0f };
                shader.setUniform4fv("u_paparazziColor", paparazziColor, 0, 4);
                float[] paparazziSeed = new float[]{ currentPaparazziSeed, currentPaparazziSeed };
                shader.setUniform2fv("u_paparazziSeed", paparazziSeed, 0, 2);
                Vector2 shaderCoords = cameraScreenToShaderScreenCoords(cameraTargetScreenPos);
                float[] paparazziOverlaySafeCircle = new float[]{ shaderCoords.x, shaderCoords.y , 100.0f };
                shader.setUniform3fv("u_paparazziOverlaySafeCircle", paparazziOverlaySafeCircle, 0, 3);
                shader.setUniformf("u_paparazziIntensity", paparazziIntensity.get());
                
                shader.setUniformf("u_time", (float)TimeUtils.timeSinceMillis(startTime) * 0.001f);
            }
            
            DrawUtil.batch.draw(screenOverlayWhiteRect, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            DrawUtil.batch.flush();
        }
        
        DrawUtil.setShader(null);
        
        super.update(deltaTime);
    }

    protected void startPaparazzi(List<String> args) {
        final float stdDuration = 2.0f;
        if(args.size() <= 1)
        {
            paparazziStartDuration = stdDuration;
        }
        else
        {
            try
            {
                paparazziStartDuration = Float.parseFloat(args.get(1));
            }
            catch(Exception e)
            {
                paparazziStartDuration = stdDuration;
            }
        }
        paparazziDurationLeft = paparazziStartDuration;
        startTime = TimeUtils.millis();
        
        currentPaparazziSeed += 0.2;
        currentPaparazziSeed = currentPaparazziSeed % Float.MAX_VALUE;
    }

}
