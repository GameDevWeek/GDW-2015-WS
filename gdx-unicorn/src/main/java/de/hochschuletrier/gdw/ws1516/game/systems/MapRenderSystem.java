package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.badlogic.gdx.utils.TimeUtils;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent.GameStateType;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;

public class MapRenderSystem extends IteratingSystem implements RainbowEvent.Listener, ChangeInGameStateEvent.Listener
{
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap<>();
    
    private static Logger logger;
    
    private TiledMap map;
    float mapWidth;
    float mapHeight;
    private TiledMapRendererGdx mapRenderer;
    private Texture backgroundTexture;
    float horizontalParallaxMultiplier;
    float verticalParallaxMultiplier;
    private Texture rainbowLayerTexture;
    
    private float rainbowTime;
    
    private float rainbowStartDuration;
    
    private boolean isPaused;
    
    @SuppressWarnings("unchecked")
    public MapRenderSystem(int priority) {
        super(Family.all().get(), priority);

        Pixmap pm = new Pixmap(1, 1, Format.RGBA8888);
        pm.setColor(Color.WHITE);
        rainbowLayerTexture = new Texture(pm);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        RainbowEvent.register(this);
        ChangeInGameStateEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        
        RainbowEvent.unregister(this);
        ChangeInGameStateEvent.unregister(this);
    }
    
    public void initialzeRenderer(TiledMap map, String backgroundGraphic, CameraSystem cameraSystem)
    {
        this.map = map;
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
        
        mapWidth = map.getWidth() * map.getTileWidth();
        mapHeight = map.getHeight() * map.getTileHeight();
        cameraSystem.setCameraBounds(0, 0, (int)mapWidth, (int)mapHeight);

        Pattern extractFileName = Pattern.compile("^.*/(.*)\\.tmx$");
        Matcher extractFileNameMatcher = extractFileName.matcher(map.getFilename());
        extractFileNameMatcher.matches();
        String mapName = extractFileNameMatcher.group(1) + "_background";
        
        // DEBUG
        System.out.println(mapName);
        
        try
        {
            backgroundTexture = Main.getInstance().getAssetManager().getTexture(mapName);
        }
        catch(NullPointerException e)
        {
            if(logger == null)
            {
                logger = LoggerFactory.getLogger(MapRenderSystem.class);
            }
            logger.error("Could not find entry " + mapName + " in images.json.", e);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if(!isPaused)
        {
            rainbowTime += deltaTime;
        }

        renderBackground(deltaTime);
        
        mapRenderer.update(deltaTime);
        for (Layer layer : map.getLayers()) {
            if(layer.isTileLayer())
                mapRenderer.render(0, 0, layer);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // No entities to process
    }
    
    protected void startRainbow()
    {
        startRainbow(GameConstants.RAINBOW_MODE_TIME);
    }
    
    protected void endRainbow()
    {
        startRainbow(0);
    }

    protected void startRainbow(List<String> args) {
        if(args.size() <= 1)
        {
            startRainbow();
        }
        else
        {
            try
            {
                startRainbow(Float.parseFloat(args.get(1)));
            }
            catch(Exception e)
            {
                startRainbow();
            }
        }
    }
    
    protected void startRainbow(float duration)
    {
        rainbowStartDuration = duration;
        rainbowTime = 0f;
    }

    @Override
    public void onRainbowCollect(Entity player) {
        startRainbow();
    }

    @Override
    public void onRainbowModeEnd(Entity player) {
        endRainbow();
    }
    
    private void renderBackground(float deltaTime)
    {
        if(backgroundTexture == null)
        {
            // no background available, early out
            return;
        }
        
        float textureSizeMultiplier = 1f;
        while(backgroundTexture.getWidth() * textureSizeMultiplier < Gdx.graphics.getWidth() || backgroundTexture.getHeight() * textureSizeMultiplier < Gdx.graphics.getHeight())
        {
            textureSizeMultiplier += 1f;
        }
        
        horizontalParallaxMultiplier = (mapWidth - backgroundTexture.getWidth() * textureSizeMultiplier)/(mapWidth - Gdx.graphics.getWidth());
        verticalParallaxMultiplier = (mapHeight - backgroundTexture.getHeight() * textureSizeMultiplier)/(mapHeight - Gdx.graphics.getHeight());
        float xPosition = (CameraSystem.getCameraPosition().x - Gdx.graphics.getWidth() * 0.5f) * horizontalParallaxMultiplier;
        float yPosition = (CameraSystem.getCameraPosition().y - Gdx.graphics.getHeight() * 0.5f) * verticalParallaxMultiplier;

        DrawUtil.draw(backgroundTexture, xPosition, yPosition, backgroundTexture.getWidth() * textureSizeMultiplier, backgroundTexture.getHeight() * textureSizeMultiplier);
        
        if(rainbowTime < rainbowStartDuration)
        {
            ShaderProgram rainbowShader = ShaderLoader.getFancyRainbowShader();
            DrawUtil.setShader(rainbowShader);
            rainbowShader.setUniformf("u_rainbowAlpha", GameConstants.RAINBOW_ALPHA);
            rainbowShader.setUniformf("u_rainbowFrequency", GameConstants.RAINBOW_FREQUENCY);
            
            // cool unused shaders
//            rainbowShader = ShaderLoader.getSimpleRainbowShader();
//            DrawUtil.setShader(rainbowShader);
//            rainbowShader.setUniformf("u_rainbowAlpha", rainbowAlpha.get());
//            rainbowShader.setUniformf("u_rainbowFrequency", rainbowFrequency.get());
//        
//            rainbowShader = ShaderLoader.getOldRainbowShader();
//            DrawUtil.setShader(rainbowShader);
//            rainbowShader.setUniformf("u_rainbowAlpha", rainbowAlpha.get());
//            rainbowShader.setUniformf("u_rainbowFrequency", rainbowFrequency.get() * 0.2f);
            
            if(rainbowShader != null)
            {
                float[] dimensions = new float[]{ Gdx.graphics.getWidth(), Gdx.graphics.getHeight() };
                rainbowShader.setUniformf("u_rainbowAmplitude", GameConstants.RAINBOW_AMPLITUDE);
                rainbowShader.setUniformf("u_startDuration", rainbowStartDuration);
                rainbowShader.setUniformf("u_durationLeft", rainbowStartDuration - rainbowTime);
                rainbowShader.setUniform2fv("u_frameDimension", dimensions, 0, 2);
                rainbowShader.setUniformf("u_time", rainbowTime);
            }
            
            DrawUtil.batch.draw(rainbowLayerTexture, CameraSystem.getCameraPosition().x - Gdx.graphics.getWidth() * 0.5f, CameraSystem.getCameraPosition().y - Gdx.graphics.getHeight() * 0.5f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            
            DrawUtil.setShader(null);
        }
    }

    @Override
    public void onPauseGameEvent(GameStateType state) {
        switch (state ) {
        case GAME_PAUSE:  
                isPaused = true;
        case GAME_PLAYER_FREEZE:  
            break;
        case GAME_PLAYING:
                isPaused = false;
            break;
        default:
            break;
        }
        
    }
}
