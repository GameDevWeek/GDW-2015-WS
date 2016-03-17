package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;

public class MapRenderSystem extends IteratingSystem implements RainbowEvent.Listener{

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    
    private long startTime;
    
    private float rainbowDurationLeft;
    private float rainbowStartDuration;
    
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap<>();
    
    private final CVarInt rainbowType = new CVarInt("fancyRainbow", 0, 0, 2, 0, "sets fancy rainbowmode");
    private final ConsoleCmd rainbow = new ConsoleCmd("rainbow", 0, "Usage: rainbow [duration] - Activates rainbow mode for [duration] seconds") { @Override public void execute(List<String> args) { startRainbow(args); } };
    private final Hotkey rainbowHotkey = new Hotkey(() -> startRainbow(), Input.Keys.R, HotkeyModifier.CTRL);
    private final Hotkey fancyRainbowHotkey = new Hotkey(() -> rainbowType.set((rainbowType.get() + 1) % 3, true), Input.Keys.T, HotkeyModifier.CTRL);
    private final CVarFloat rainbowFrequency = new CVarFloat("rainbowFrequency", GameConstants.RAINBOW_FREQUENCY, 0.0f, Float.MAX_VALUE, 0, "");
    private final CVarFloat rainbowAlpha = new CVarFloat("rainbowAlpha", GameConstants.RAINBOW_ALPHA, 0.0f, Float.MAX_VALUE, 0, "");
    
    
    @SuppressWarnings("unchecked")
    public MapRenderSystem(int priority) {
        super(Family.all().get(), priority);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        RainbowEvent.register(this);
        
        Main.getInstance().console.register(rainbow);
        Main.getInstance().console.register(rainbowFrequency);
        Main.getInstance().console.register(rainbowAlpha);
        Main.getInstance().console.register(rainbowType);
        rainbowHotkey.register();
        fancyRainbowHotkey.register();
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        
        RainbowEvent.unregister(this);
        
        Main.getInstance().console.unregister(rainbow);
        Main.getInstance().console.unregister(rainbowFrequency);
        Main.getInstance().console.unregister(rainbowAlpha);
        Main.getInstance().console.unregister(rainbowType);
        rainbowHotkey.unregister();
        fancyRainbowHotkey.unregister();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if(rainbowDurationLeft > 0.0f)
        {
            rainbowDurationLeft -= deltaTime;
            ShaderProgram rainbowShader = null;
            switch(rainbowType.get())
            {
            case 0:
                rainbowShader = ShaderLoader.getFancyRainbowShader();
                DrawUtil.setShader(rainbowShader);
                rainbowShader.setUniformf("u_rainbowAlpha", rainbowAlpha.get());
                rainbowShader.setUniformf("u_rainbowFrequency", rainbowFrequency.get());
                break;
            case 1:
                rainbowShader = ShaderLoader.getSimpleRainbowShader();
                DrawUtil.setShader(rainbowShader);
                rainbowShader.setUniformf("u_rainbowAlpha", rainbowAlpha.get());
                rainbowShader.setUniformf("u_rainbowFrequency", rainbowFrequency.get());
                break;
            default:
                rainbowShader = ShaderLoader.getOldRainbowShader();
                DrawUtil.setShader(rainbowShader);
                rainbowShader.setUniformf("u_rainbowAlpha", rainbowAlpha.get());
                rainbowShader.setUniformf("u_rainbowFrequency", rainbowFrequency.get() * 0.2f);
                break;
            }
            if(rainbowShader != null)
            {
                float[] dimensions = new float[]{ Gdx.graphics.getWidth(), Gdx.graphics.getHeight() };
                rainbowShader.setUniformf("u_startDuration", rainbowStartDuration);
                rainbowShader.setUniformf("u_durationLeft", rainbowDurationLeft);
                rainbowShader.setUniform2fv("u_frameDimension", dimensions, 0, 2);
                rainbowShader.setUniformf("u_time", (float)TimeUtils.timeSinceMillis(startTime) * 0.001f);
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
    
    protected void startRainbow()
    {
        List<String> duration = new ArrayList<>();
        duration.add(GameConstants.RAINBOW_DURATION + "");
        startRainbow(duration);
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

    @Override
    public void onRainbowCollect() {
        startRainbow();
    }
}
