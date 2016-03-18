package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.PaparazziShootEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.CameraTargetComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;



public class EffectsRenderSystem extends IteratingSystem implements PaparazziShootEvent.Listener {
    
    private long startTime;
    
    private float paparazziDurationLeft;
    private float paparazziStartDuration;
    
    private Vector2 cameraTargetScreenPos;
    
    private Texture screenOverlayWhiteRect;
    
    private final ConsoleCmd paparazziConsole = new ConsoleCmd("pap", 0, "Usage: pap (duration) - Activates paparazzi mode for (duration) seconds") { @Override public void execute(List<String> args) { consolePaparazzi(args); } };
    private float paparazziIntensity = 0.0f;
    private Vector2 currentPaparazziSeed;
    

    private final Hotkey paparazziHotkey = new Hotkey(()->PaparazziShootEvent.emit(1.0f), Input.Keys.F9,
            HotkeyModifier.CTRL);

    @SuppressWarnings("unchecked")
    public EffectsRenderSystem(int priority) {
        super(Family.all(CameraTargetComponent.class, PositionComponent.class).get(), priority);
        
        cameraTargetScreenPos = new Vector2( (float) (Gdx.graphics.getWidth()* 0.5), (float) (Gdx.graphics.getHeight() * 0.5) );
        
        // create white rectangle texture to initiate draw call
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
    public void onPaparazziShootEvent(float distance) {
        startPaparazzi(distance, 2.0f, new Vector2(distance, distance*2));
        System.out.println(distance);
    }
    
    @Override
    public void addedToEngine(Engine engine) {

        PaparazziShootEvent.register(this);

        paparazziHotkey.register();
        
        //DEBUG
        Main.getInstance().console.register(paparazziConsole);
        
        super.addedToEngine(engine);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        
        PaparazziShootEvent.unregister(this);

        paparazziHotkey.unregister();
        
        //DEBUG
        Main.getInstance().console.unregister(paparazziConsole);
        
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
                
                // color set as RGBA [0.0, 1.0]. alpha is used as maximum result alpha for overlay.
                float[] paparazziColor = new float[]{ 1.0f, 1.0f, 1.0f, 1.0f };
                shader.setUniform4fv("u_paparazziColor", paparazziColor, 0, 4);
                float[] paparazziSeed = new float[]{ currentPaparazziSeed.x, currentPaparazziSeed.y };
                shader.setUniform2fv("u_paparazziSeed", paparazziSeed, 0, 2);
                Vector2 shaderCoords = cameraScreenToShaderScreenCoords(cameraTargetScreenPos);
                float[] paparazziOverlaySafeCircle = new float[]{ shaderCoords.x, shaderCoords.y , 100.0f };
                shader.setUniform3fv("u_paparazziOverlaySafeCircle", paparazziOverlaySafeCircle, 0, 3);
                shader.setUniformf("u_paparazziIntensity", paparazziIntensity);
            }
            
            DrawUtil.batch.draw(screenOverlayWhiteRect, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            DrawUtil.batch.flush();
        }
        
        DrawUtil.setShader(null);
        
        super.update(deltaTime);
    }
    
    private void startPaparazzi(float distance, float duration, Vector2 seed)
    {
        paparazziDurationLeft = paparazziStartDuration = duration;
        
        currentPaparazziSeed = seed;
        
        paparazziIntensity = (1 - Math.max((distance - 100) / 500, 0));
    }

    protected void consolePaparazzi(List<String> args) {
        float duration;
        final float stdDuration = 2.0f;
        if(args.size() <= 1)
        {
            duration = stdDuration;
        }
        else
        {
            try
            {
                duration = Float.parseFloat(args.get(1));
            }
            catch(Exception e)
            {
                duration = stdDuration;
            }
        }
        currentPaparazziSeed.add(3.6f, 7.8f);
        startPaparazzi(500, duration, currentPaparazziSeed);
    }

}
