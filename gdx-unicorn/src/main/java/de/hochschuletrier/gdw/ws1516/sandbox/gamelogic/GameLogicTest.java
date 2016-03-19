package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.game.components.SoundEmitterComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyVisionSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.HitPointManagementSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.RespawnSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.SoundSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.enemyStates.FollowPathEnemyState;
import de.hochschuletrier.gdw.ws1516.sandbox.SandboxGame;

/**
 *
 * @author Tobi
 */
public class GameLogicTest extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(GameLogicTest.class);

    
    /// Hotkeys
    private Entity unicorn = null;
    private final Hotkey restart = new Hotkey(() -> {GameRespawnEvent.emit();
        }, Input.Keys.F3,HotkeyModifier.CTRL);
    private Hotkey playSound = new Hotkey(() -> {SoundEvent.emit("helicopter",unicorn,true);
    }, Input.Keys.F1,HotkeyModifier.CTRL);
    private Hotkey stopSound = new Hotkey(() -> {SoundEvent.stopSound(unicorn);
    }, Input.Keys.F2,HotkeyModifier.CTRL);
    
    private SandBoxEventLogger sbeLogger = new SandBoxEventLogger();

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );
    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private float totalMapWidth, totalMapHeight;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private PhysixBodyComponent playerBody;
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap();

    private EntitySystem respawnSystem = new RespawnSystem();

    private final SoundSystem soundSystem = new SoundSystem(null);

    private final HitPointManagementSystem hitPointSystem = new HitPointManagementSystem();

    private final DummyEnemyExecutionSystem dummyEnemySystem = new DummyEnemyExecutionSystem();
    
    private final EnemyHandlingSystem enemyHandlingSystem = new EnemyHandlingSystem();

    private final EntitySystem enemyVisionSystem = new EnemyVisionSystem();


    private final EntitySystem updatePositionSystem = new UpdatePositionSystem();
    
    public GameLogicTest() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(respawnSystem );
        engine.addSystem(soundSystem);
        engine.addSystem(hitPointSystem);
        engine.addSystem(enemyHandlingSystem);
        engine.addSystem(dummyEnemySystem);
        engine.addSystem(enemyVisionSystem );
        engine.addSystem(updatePositionSystem );
    }

    @Override
    public void init(AssetManagerX assetManager) {
        
        
        map = loadMap("data/maps/demo.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);

        // Generate static world
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blocked", false),
                (Rectangle rect) -> addShape(rect, tileWidth, tileHeight));

        // create a simple player ball

        
        unicorn = createDummyUnicorn();
        playSound.register();
        stopSound.register();
        restart.register();     

        createDummyStartPoint();
        createDummyStartPoint();

        createDummyEnemy();

        createDummyStartPoint();
        
        // Setup camera
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        totalMapWidth = map.getWidth() * map.getTileWidth();
        totalMapHeight = map.getHeight() * map.getTileHeight();
        camera.setBounds(0, 0, totalMapWidth, totalMapHeight);
        camera.updateForced();
        Main.getInstance().addScreenListener(camera);
    }
    private void addShape(Rectangle rect, int tileWidth, int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(width, height));
    }

    @Override
    public void dispose() {
        Main.getInstance().removeScreenListener(camera);
        tilesetImages.values().forEach(Texture::dispose);
    }

    public TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    @Override
    public void update(float delta) {
        camera.bind();
        for (Layer layer : map.getLayers()) {
            mapRenderer.render(0, 0, layer);
        }
        engine.update(delta);
        
        mapRenderer.update(delta);
        camera.update(delta);

        if(playerBody != null) {
            float speed = 10000.0f;
            float velX = 0, velY = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                velX -= delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velX += delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                velY -= delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                velY += delta * speed;
            }

            playerBody.setLinearVelocity(velX, velY);
            camera.setDestination(playerBody.getPosition());
        }
    }
    
    private Entity createDummyUnicorn()
    {

        Entity player = engine.createEntity();
        ScoreComponent scoreComp = engine.createComponent(ScoreComponent.class);
        player.add(scoreComp);
        PlayerComponent playerComp = engine.createComponent(PlayerComponent.class);
        player.add(playerComp);
        PositionComponent positionComp = engine.createComponent(PositionComponent.class);
        player.add(positionComp);
        StartPointComponent startPositionComp = engine.createComponent(StartPointComponent.class);
        player.add(startPositionComp);
        SoundEmitterComponent soundComponent = engine.createComponent(SoundEmitterComponent.class);
        player.add(soundComponent);
        
        
        
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        player.add(modifyComponent);        
        modifyComponent.schedule(() -> {
            playerBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(100, 100).fixedRotation(true);
            playerBody.init(bodyDef, physixSystem, player);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(30);
            playerBody.createFixture(fixtureDef);
            player.add(playerBody);
        });
        engine.addEntity(player);
        
        
        return player;
    }

    private void createDummyStartPoint() {

        Entity start = engine.createEntity();
        StartPointComponent startComp = engine.createComponent(StartPointComponent.class);
        start.add(startComp);
        engine.addEntity(start);
        
    }


    private void createDummyEnemy() {

        Entity enemy = engine.createEntity();

        StartPointComponent startComp = engine.createComponent(StartPointComponent.class);
        enemy.add(startComp);
        
        PositionComponent posComp = engine.createComponent(PositionComponent.class);
        enemy.add(posComp);
        
        PathComponent pathComp = engine.createComponent(PathComponent.class);
        pathComp.points.add(new Vector2(800,100));
        pathComp.points.add(new Vector2(200,100));
        enemy.add(pathComp);

        EnemyBehaviourComponent behaviour = engine.createComponent(EnemyBehaviourComponent.class);
        behaviour.currentState = new FollowPathEnemyState();
        enemy.add(behaviour);

        EnemyTypeComponent enemyType = engine.createComponent(EnemyTypeComponent.class);
        enemy.add(enemyType);
        
        
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        enemy.add(modifyComponent);        
        modifyComponent.schedule(() -> {
            PhysixBodyComponent enemyBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(200, 100).fixedRotation(true);
            enemyBody.init(bodyDef, physixSystem, enemy);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(30);
            enemyBody.createFixture(fixtureDef);
            enemy.add(enemyBody);
        });
        
        engine.addEntity(enemy);
        
    }

}
