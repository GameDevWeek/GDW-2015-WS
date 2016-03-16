package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.HashMap;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.TimeUtils;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityLoader;
import de.hochschuletrier.gdw.ws1516.game.utils.MapLoader;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysicsLoader;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;

public class MapRenderSystem extends IteratingSystem {

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    
    private long startTime;
    
    private float rainbowDurationLeft;
    private float rainbowStartDuration;
    
    private float paparazziDurationLeft;
    private float paparazziStartDuration;
    
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap<>();
    
    private final ConsoleCmd rainbow = new ConsoleCmd("rainbow", 0, "Usage: rainbow [duration] - Activates rainbow mode for [duration] seconds") { @Override public void execute(List<String> args) { startRainbow(args); } };
    private final CVarFloat rainbowFrequency = new CVarFloat("rainbowFrequency", GameConstants.RAINBOW_FREQUENCY, 0.0f, Float.MAX_VALUE, 0, "");
    private final CVarFloat rainbowAlpha = new CVarFloat("rainbowAlpha", GameConstants.RAINBOW_ALPHA, 0.0f, Float.MAX_VALUE, 0, "");
    
    private final ConsoleCmd paparazzi = new ConsoleCmd("pap", 0, "Usage: paparazzi [duration] - Activates paparazzi mode for [duration] seconds") { @Override public void execute(List<String> args) { startPaparazzi(args); } };
    private final CVarFloat paparazziIntensity = new CVarFloat("paparazziIntensity", GameConstants.PAPARAZZI_INTENSITY, 0.0f, Float.MAX_VALUE, 0, "");
    private final CVarFloat paparazziAlpha = new CVarFloat("paparazziAlpha", GameConstants.PAPARAZZI_ALPHA, 0.0f, Float.MAX_VALUE, 0, "");
    
    @SuppressWarnings("unchecked")
    public MapRenderSystem(int priority) {
        super(Family.all().get(), priority);

        // DEBUG
        Main.getInstance().console.register(rainbow);
        Main.getInstance().console.register(rainbowFrequency);
        Main.getInstance().console.register(rainbowAlpha);
        Main.getInstance().console.register(paparazzi);
        
        Main.getInstance().console.register(paparazziIntensity);
        Main.getInstance().console.register(paparazziAlpha);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if(rainbowDurationLeft > 0.0f)
        {
            rainbowDurationLeft -= deltaTime;
            ShaderProgram rainbowShader = ShaderLoader.getRainbowShader();
            DrawUtil.setShader(rainbowShader);
            if(rainbowShader != null)
            {
                float[] dimensions = new float[]{ Gdx.graphics.getWidth(), Gdx.graphics.getHeight() };
                rainbowShader.setUniformf("u_startDuration", rainbowStartDuration);
                rainbowShader.setUniformf("u_durationLeft", rainbowDurationLeft);
                rainbowShader.setUniform2fv("u_frameDimension", dimensions, 0, 2);
                rainbowShader.setUniformf("u_rainbowAlpha", rainbowAlpha.get());
                rainbowShader.setUniformf("u_rainbowFrequency", rainbowFrequency.get());
                rainbowShader.setUniformf("u_time", (float)TimeUtils.timeSinceMillis(startTime) * 0.001f);
            }
        } else if (paparazziDurationLeft > 0.0f) {
            
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
                shader.setUniformf("u_paparazziIntensity", rainbowFrequency.get());
                shader.setUniformf("u_time", (float)TimeUtils.timeSinceMillis(startTime) * 0.001f);
            }

        }
        
        mapRenderer.update(deltaTime);
        for (Layer layer : map.getLayers()) {
            mapRenderer.render(0, 0, layer);
        }

                
        
        DrawUtil.setShader(null);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // No entities to process
    }

    protected void startRainbow(List<String> args) {
        if(args.size() <= 1)
        {
            rainbowStartDuration = 5.0f;
        }
        else
        {
            try
            {
                rainbowStartDuration = Float.parseFloat(args.get(1));
            }
            catch(Exception e)
            {
                rainbowStartDuration = 5.0f;
            }
        }
        rainbowDurationLeft = rainbowStartDuration;
        startTime = TimeUtils.millis();
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
    
    public void initialzeRenderer(TiledMap map, CameraSystem cameraSystem)
    {
        this.map = map;
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
        
        int totalMapWidth = map.getWidth() * map.getTileWidth();
        int totalMapHeight = map.getHeight() * map.getTileHeight();
        cameraSystem.setCameraBounds(0, 0, totalMapWidth, totalMapHeight);
    }
}
