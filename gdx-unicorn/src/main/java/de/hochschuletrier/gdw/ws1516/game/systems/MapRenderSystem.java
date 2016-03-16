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
    
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap<>();
    private final PooledEngine engine;
    private final Game game;
    
    private final ConsoleCmd rainbow = new ConsoleCmd("rainbow", 0, "Usage: rainbowmode [duration] - Activates rainbowmode for [duration] seconds") { @Override public void execute(List<String> args) { startRainbow(args); } };
    private final CVarFloat rainbowFrequency = new CVarFloat("rainbowFrequency", GameConstants.RAINBOW_FREQUENCY, 0.0f, Float.MAX_VALUE, 0, "");
    private final CVarFloat rainbowAlpha = new CVarFloat("rainbowAlpha", GameConstants.RAINBOW_MODE_ALPHA, 0.0f, Float.MAX_VALUE, 0, "");
    
    @SuppressWarnings("unchecked")
    public MapRenderSystem(PooledEngine engine, Game game, int priority) {
        super(Family.all().get(), priority);
        
        this.engine = engine;
        this.game = game;

        Main.getInstance().console.register(rainbow);
        Main.getInstance().console.register(rainbowFrequency);
        Main.getInstance().console.register(rainbowAlpha);
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
                rainbowShader.setUniformf("u_rainbowAlpha", 0.6f);
                rainbowShader.setUniformf("u_rainbowFrequency", 2.0f);
                rainbowShader.setUniformf("u_time", (float)TimeUtils.timeSinceMillis(startTime) * 0.001f);
            }
        }
        
        mapRenderer.update(deltaTime);
        for (Layer layer : map.getLayers()) {
            mapRenderer.render(0, 0, layer);
        }

        DrawUtil.setShader(null);
    }


    /**
     * 
     * @param filename
     *            filepath to the map that is to be loaded
     * @param cameraSystem
     *            for updating the bounds of the camera
     */
    public void loadMap(String filename, CameraSystem cameraSystem) {
        // Map laden
        try {
            map = new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Map konnte nicht geladen werden: " + filename);
        }

        // Wenn map geladen wurde
        if (map != null) {
            
            // Map parsen
            MapLoader[] mapLoaders = { new PhysicsLoader(), new EntityLoader() };
            for (MapLoader mapLoader : mapLoaders) {
                mapLoader.parseMap(map, game, engine);
            }
            
            initialzeRenderer(cameraSystem);
        }
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
    
    private void initialzeRenderer(CameraSystem cameraSystem)
    {
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
