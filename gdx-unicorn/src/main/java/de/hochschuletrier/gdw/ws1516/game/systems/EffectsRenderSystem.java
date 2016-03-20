package de.hochschuletrier.gdw.ws1516.game.systems;

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

import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.PaparazziShootEvent;
import de.hochschuletrier.gdw.ws1516.events.ActivateSafePointEvent;
import de.hochschuletrier.gdw.ws1516.events.TriggerEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.CameraTargetComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;


public class EffectsRenderSystem extends IteratingSystem implements PaparazziShootEvent.Listener, DeathEvent.Listener, TriggerEvent.Listener, ActivateSafePointEvent.Listener, ScreenListener {

    private Texture screenOverlayWhiteRect;

    private float[] frameDimensions;
    
    private Vector2 cameraTargetScreenPos;
    
    // color set as RGBA [0.0, 1.0]. alpha is used as maximum result alpha for overlay.
    private final float[] paparazziColor = new float[]{ 1.0f, 1.0f, 1.0f, 0.9f };
    private final float paparazziOverlaySafeCircleRadius;
            
    private float paparazziEffectIntensity;
    private float paparazziEffectRemainingDuration;
    private float paparazziEffectDuration;
    private float[] paparazziEffectSeed;
    
    // color set as RGBA [0.0, 1.0]. alpha is used as maximum result alpha for overlay.
    private final float[] caveLightColor = new float[]{ 1.0f, 0.8f, 0.0f, 0.6f };
    private final float[] caveDarknessColor = new float[]{ 0.0f, 0.0f, 0.0f, 0.3f };
    
    private static final int MAX_LIGHT_UNIFORMS = 15;
    private static final float CAVE_GLOW_DURATION = 6.0f; // in seconds
    private static final float CAVE_INTRO_DURATION = 1.0f; // in seconds
    private static final float CAVE_OUTRO_DURATION = 1.0f; // in seconds
    private boolean isInsideCave;
    private boolean isActivatedSafePointInsideCave;
    
    private boolean isInCaveIntro;
    private boolean isInCaveOutro;
    private float caveTime;
    private float caveEffectIntensity;
    private float caveEffectLightGlowIntensity;

    //DEBUG
    // hotkey 1: about 1.0 intensity
    private final Hotkey paparazzi1Hotkey = new Hotkey(()->PaparazziShootEvent.emit((float)Math.random()), Input.Keys.F9,
            HotkeyModifier.CTRL);
    // hotkey 0: about 0.2 intensity
    private final Hotkey paparazzi0Hotkey = new Hotkey(()->PaparazziShootEvent.emit((float) (Math.random() + GameConstants.GLOBAL_VISION * GameConstants.UNICORN_SIZE)), Input.Keys.F8,
            HotkeyModifier.CTRL);

    
    
    @SuppressWarnings("unchecked")
    public EffectsRenderSystem(int priority) {
        super(Family.all(CameraTargetComponent.class, PositionComponent.class).get(), priority);
        
        // create white rectangle texture to initiate draw call
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.screenOverlayWhiteRect = new Texture(pixmap);
        
        // set initial values
        // paparazzi
        frameDimensions = new float[]{ Gdx.graphics.getWidth(), Gdx.graphics.getHeight() };
        cameraTargetScreenPos = new Vector2( (float) (Gdx.graphics.getWidth()* 0.5), (float) (Gdx.graphics.getHeight() * 0.5) );
        paparazziOverlaySafeCircleRadius = (float) (GameConstants.UNICORN_SIZE * 0.8);
        resetPaparazzi();
        
        // cave
        isActivatedSafePointInsideCave = false;
        isInCaveIntro = false;
        isInCaveOutro = false;
        caveEffectLightGlowIntensity = caveEffectIntensity = 0.0f;
        caveTime = 0.0f;
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // save camera target position to set paparazzi effect save circle around player        
        PositionComponent targetPos = ComponentMappers.position.get(entity);
        cameraTargetScreenPos = CameraSystem.worldToScreenCoordinates(targetPos.x, targetPos.y);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        
        Main.getInstance().addScreenListener(this);

        PaparazziShootEvent.register(this);
        DeathEvent.register(this);
        TriggerEvent.register(this);

        //DEBUG
        paparazzi1Hotkey.register();
        paparazzi0Hotkey.register();
        
        super.addedToEngine(engine);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        
        Main.getInstance().removeScreenListener(this);
        
        PaparazziShootEvent.unregister(this);
        DeathEvent.unregister(this);
        TriggerEvent.unregister(this);

        //DEBUG
        paparazzi1Hotkey.unregister();
        paparazzi0Hotkey.unregister();
        
        super.removedFromEngine(engine);
    }

    @Override
    public void resize(int width, int height) {
        frameDimensions = new float[]{ width, height };
    }
    
    // PAPARAZZI
    
    @Override
    public void onPaparazziShootEvent(float distance) {
        startPaparazzi(distance, GameConstants.PAPARAZZI_DURATION, distance, distance*2);
    }
    
    private void startPaparazzi(float distance, float duration, float seed1, float seed2)
    {
        paparazziEffectDuration = paparazziEffectRemainingDuration = duration;
        paparazziEffectSeed = new float[]{ seed1, seed2 };

        float maxRange = GameConstants.GLOBAL_VISION * GameConstants.UNICORN_SIZE;
        float minRange = GameConstants.UNICORN_SIZE;
        paparazziEffectIntensity = (1 - Math.max((distance - minRange) / maxRange, 0));
    }
    
    private void resetPaparazzi()
    {
        paparazziEffectDuration = paparazziEffectRemainingDuration = paparazziEffectIntensity = 0.0f;
    }

    @Override
    public void onDeathEvent(Entity entity) {
        if (ComponentMappers.player.has(entity))
        {
            resetPaparazzi();
            resetCaveOnDeath();
        }
    }
    
    // CAVE ENTER/CAVE EXIT    
    private void startCave()
    {
        isInsideCave = true;
        
        isInCaveIntro = true;
        isInCaveOutro = false;
        
        if (!isInCaveOutro)
        {
            caveEffectLightGlowIntensity = caveEffectIntensity = 0.0f;
            caveTime = 0.001f; // start shader usage
        }
    }
    
    private void stopCave()
    {
        isInsideCave = false;
        
        isInCaveIntro = false;
        isInCaveOutro = true;
    }
    
    private void resetCaveOnDeath()
    {
        isInsideCave = isActivatedSafePointInsideCave;
        if (!isInsideCave) {
            isInCaveIntro = false;
            isInCaveOutro = false;
            
            caveEffectLightGlowIntensity = caveEffectIntensity = 0.0f;
            caveTime = 0.0f;
        }
    }
    
    private void calculateNextCaveIntensityStep(float deltaTime) {
        
        if (isInCaveIntro)
        {
            caveEffectIntensity += deltaTime / CAVE_INTRO_DURATION;
            if (caveEffectIntensity > 1.0f)
            {
                caveEffectIntensity = 1.0f;
                isInCaveIntro = false;
            }
        }
        else if (isInCaveOutro)
        {
            caveEffectIntensity -= deltaTime / CAVE_OUTRO_DURATION;
            if (caveEffectIntensity < 0.0f)
            {
                caveEffectIntensity = 0.0f;
                isInCaveOutro = false;
                caveTime = 0.0f;
            }
        }
        else
        {
            caveEffectIntensity = 1.0f;
        }
        
        if (caveEffectIntensity > 0.0f)
        {
            caveTime += deltaTime;
            caveEffectLightGlowIntensity = (float) Math.sin((caveTime / CAVE_GLOW_DURATION * Math.PI) % Math.PI);
        }
        System.out.println(caveEffectIntensity);
    }

    @Override
    public void onActivateCheckPointEvent(Entity unicorn, Entity safePoint) {
        isActivatedSafePointInsideCave = isInsideCave;
    }
    
    public void onTriggerEvent(TriggerEvent.Action action, Entity triggeringEntity) {
        
        switch (action) {
            case CAVE_ENTER:
                if (!isInsideCave) {
                    startCave();
                }
                break;
            case CAVE_EXIT:
                if (isInsideCave) {
                    stopCave();
                }
                break;
        }
        
    };

    private void setLightsAsUniform(ShaderProgram shader) {
        float[] lightSourceCoords = CaveLightsRenderSystem.getMapLightsInViewport(MAX_LIGHT_UNIFORMS);
        shader.setUniformf("u_numOfLights", lightSourceCoords[0]);
        for (int i = 0; i < MAX_LIGHT_UNIFORMS; ++i)
        {
            shader.setUniform2fv("u_lightSource" + i, lightSourceCoords, 1+ i * 2, 2);
        }
    }
    
    
    // UPDATE
    
    private Vector2 cameraScreenToShaderScreenCoords(Vector2 in)
    {
        return new Vector2(in.x, Gdx.graphics.getHeight() - in.y);
    }
    
    @Override
    public void update(float deltaTime) {
        
        if (caveTime > 0.0f) {
            
            calculateNextCaveIntensityStep(deltaTime);
            ShaderProgram shader = ShaderLoader.getCaveShader();
            DrawUtil.setShader(shader);
            if(shader != null)
            {
                shader.setUniformf("u_caveIntensity", caveEffectIntensity);
                shader.setUniformf("u_lightGlowIntensity", caveEffectLightGlowIntensity);
                shader.setUniformf("u_lightGlowRadius", GameConstants.CAVE_LIGHT_GLOW_RADIUS);
                shader.setUniform4fv("u_lightColor", caveLightColor, 0, 4);
                shader.setUniform4fv("u_darknessColor", caveDarknessColor, 0, 4);
                
                setLightsAsUniform(shader);
            }
            
            DrawUtil.batch.draw(screenOverlayWhiteRect, CameraSystem.getViewportTopLeft().x, CameraSystem.getViewportTopLeft().y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            DrawUtil.batch.flush();
        }
        
        if (paparazziEffectRemainingDuration > 0.0f) {
                    
            paparazziEffectRemainingDuration -= deltaTime;
            ShaderProgram shader = ShaderLoader.getPaparazziShader();
            DrawUtil.setShader(shader);
            if(shader != null)
            {
                shader.setUniform2fv("u_frameDimension", frameDimensions, 0, 2);
                shader.setUniform4fv("u_paparazziColor", paparazziColor, 0, 4);
                
                shader.setUniformf("u_effectDuration", paparazziEffectDuration);
                shader.setUniformf("u_passedEffectTime", paparazziEffectDuration - Math.max(paparazziEffectRemainingDuration, 0.0f));
                shader.setUniformf("u_paparazziIntensity", paparazziEffectIntensity);
                shader.setUniform2fv("u_paparazziSeed", paparazziEffectSeed, 0, 2);
                
                Vector2 shaderCoords = cameraScreenToShaderScreenCoords(cameraTargetScreenPos);
                float[] paparazziOverlaySafeCircle = new float[]{ shaderCoords.x, shaderCoords.y , paparazziOverlaySafeCircleRadius };
                shader.setUniform3fv("u_paparazziOverlaySafeCircle", paparazziOverlaySafeCircle, 0, 3);
            }
            
            DrawUtil.batch.draw(screenOverlayWhiteRect, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            DrawUtil.batch.flush();
        }
        
        DrawUtil.setShader(null);
        
        super.update(deltaTime);
    }
    
   

}
