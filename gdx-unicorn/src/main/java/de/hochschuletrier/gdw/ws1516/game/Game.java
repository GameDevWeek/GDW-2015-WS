package de.hochschuletrier.gdw.ws1516.game;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRestartEvent;
import de.hochschuletrier.gdw.ws1516.events.PaparazziShootEvent;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent.GameStateType;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent;
import de.hochschuletrier.gdw.ws1516.events.ScoreBoardEvent.ScoreType;
import de.hochschuletrier.gdw.ws1516.events.TriggerEvent.Action;
import de.hochschuletrier.gdw.ws1516.game.components.BlockingGumComponent;
import de.hochschuletrier.gdw.ws1516.game.components.BubblegumSpitComponent;
import de.hochschuletrier.gdw.ws1516.game.components.BulletComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlatformComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.BlockingGumListener;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.BubblegumSpitListener;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.BulletListener;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.PlayerContactListener;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.PlayerPlatformListener;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ws1516.game.systems.AnimationEventHandlerSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.BlockingGumSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.BubbleGlueSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.BubblegumSpitSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.BulletSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.CaveLightsRenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.DeathAnimationSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.EffectsRenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyVisionSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.HitPointManagementSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.HudRenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.KeyboardInputSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.MapRenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.NameSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.PlatformHandlingSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.PlatformSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.PlayerStateSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.RenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.RespawnSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.ScoreSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.SoundSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.SplatterSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.TriggerSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.UpdatePlatformPositionSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityCreator;
import de.hochschuletrier.gdw.ws1516.game.utils.EntityLoader;
import de.hochschuletrier.gdw.ws1516.game.utils.MapLoader;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysicsLoader;
import de.hochschuletrier.gdw.ws1516.menu.LevelSelectionPage;
import de.hochschuletrier.gdw.ws1516.menu.MenuPageOptions;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.DummyEnemyExecutionSystem;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent;

public class Game extends InputAdapter implements ChangeInGameStateEvent.Listener  {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    
    private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1,
            HotkeyModifier.CTRL);
    private final Hotkey scoreCheating = new Hotkey(() -> ScoreBoardEvent.emit(ScoreType.BONBON, 1), Input.Keys.F2,
            HotkeyModifier.CTRL);
    private final Hotkey winGameCheat = new Hotkey(this::cheatWin, Input.Keys.F6,
            HotkeyModifier.CTRL);
    private Hotkey healCheating = null;
    private Hotkey rainbow=null;
    

    private static GameStateType engineState;



    private final PooledEngine engine = new PooledEngine(GameConstants.ENTITY_POOL_INITIAL_SIZE,
            GameConstants.ENTITY_POOL_MAX_SIZE, GameConstants.COMPONENT_POOL_INITIAL_SIZE,
            GameConstants.COMPONENT_POOL_MAX_SIZE);

    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX);
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(
            GameConstants.PRIORITY_DEBUG_WORLD);
    private final CameraSystem cameraSystem = new CameraSystem(GameConstants.PRIORITY_CAMERA);
    private final CaveLightsRenderSystem caveLightsRenderSystem = new CaveLightsRenderSystem(GameConstants.PRIORITY_CAVE_LIGHTS_RENDERING);
    private final RenderSystem renderSystem = new RenderSystem(GameConstants.PRIORITY_RENDERING);
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(
            GameConstants.PRIORITY_PHYSIX + 1);

    private final NameSystem nameSystem = new NameSystem(GameConstants.PRIORITY_NAME);

    private final KeyboardInputSystem keyBoardInputSystem= new KeyboardInputSystem(GameConstants.PRIORITY_INPUT);
    private final MovementSystem movementSystem=new MovementSystem(GameConstants.PRIORITY_MOVEMENT);
    private final BubbleGlueSystem bubbleGlueSystem = new BubbleGlueSystem(engine);
     
    private final HudRenderSystem hudRenderSystem = new HudRenderSystem(GameConstants.PRIORITY_HUD);
    
    private final MapRenderSystem mapRenderSystem = new MapRenderSystem(GameConstants.PRIORITY_MAP_RENDERING);
    private final AnimationEventHandlerSystem animationEventHandlerSystem = new AnimationEventHandlerSystem(GameConstants.PRIORITY_ANIMATION_EVENTS);
    
    private final SplatterSystem splatterSystem = new SplatterSystem(GameConstants.PRIORITY_SPLATTER);
    private final EffectsRenderSystem effectsRenderSystem = new EffectsRenderSystem(GameConstants.PRIORITY_EFFECTS_RENDERING);
    
    private final DeathAnimationSystem deathAnimationSystem = new DeathAnimationSystem(GameConstants.PRIORITY_DEATH_ANIMATION);

    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json",
            Game.class);


    private final TriggerSystem triggerSystem = new TriggerSystem(this);
    private final EntitySystem respawnSystem = new RespawnSystem();
    private final SoundSystem soundSystem = new SoundSystem(null);
    private final HitPointManagementSystem hitPointSystem = new HitPointManagementSystem(this);
    private final DummyEnemyExecutionSystem dummyEnemySystem = new DummyEnemyExecutionSystem();    
    private final EnemyHandlingSystem enemyHandlingSystem = new EnemyHandlingSystem(nameSystem);
    private final EntitySystem enemyVisionSystem = new EnemyVisionSystem();
    private final ScoreSystem scoreBoardSystem = new ScoreSystem();
    private final PlayerStateSystem playerStateSystem = new PlayerStateSystem();
    private final BubblegumSpitSystem bubblegumSpitSystem = new BubblegumSpitSystem(engine);
    private final UpdatePlatformPositionSystem updatePlatformPositionSystem = new UpdatePlatformPositionSystem();
    private final PlatformSystem platformSystem = new PlatformSystem();
    private final PlatformHandlingSystem platformHandlingSystem = new PlatformHandlingSystem(nameSystem);
    private final BulletSystem bulletSystem = new BulletSystem(engine);
    private final BlockingGumSystem blockingGumSystem = new BlockingGumSystem(engine);
    
    /// Systems not update while FreezeGame 
    private final EntitySystem[] updateOnFreeze = {              
            renderSystem,
            mapRenderSystem,
            hudRenderSystem,
            respawnSystem,
            cameraSystem,
            effectsRenderSystem,
            animationEventHandlerSystem,
            splatterSystem,
            soundSystem 
    };
    /// Systems  updated while PauseGame 
    private final EntitySystem[] updateOnPause = {            
            renderSystem,
            mapRenderSystem,
            hudRenderSystem
    };
    private EntitySystem[] updateOnPlaying;
    
    
    private TiledMap map;
    
    public Game() {
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE) {
            togglePhysixDebug.register();
            scoreCheating.register();
            winGameCheat.register();
        } else {
            physixDebugRenderSystem.setProcessing(false);
        }
        ChangeInGameStateEvent.register(this);
    }

    public void dispose() {
        ChangeInGameStateEvent.unregister(this);
        togglePhysixDebug.unregister();
        scoreCheating.unregister();
//        rainbow.unregister();
//        healCheating.unregister();
        winGameCheat.unregister();
        Main.getInstance().console.unregister(physixDebug);
        
        engine.removeAllEntities();
        
        // Stupid Engine does not have a removeAllSystems()
        ArrayList<EntitySystem> list = new ArrayList<EntitySystem>();
        for (EntitySystem system : engine.getSystems()) {
            list.add(system);
        }
        for (EntitySystem system : list) {
            engine.removeSystem(system);
        }
    }

    public void init(AssetManagerX assetManager, String mapFilename) {
        engineState = GameStateType.GAME_PLAYING;
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));
        addSystems();
        addContactListeners();
        setupPhysixWorld();
        entityFactory.init(engine, assetManager);

        // EntityCreator
        EntityCreator.setEngine(engine);
        EntityCreator.setGame(this);
        EntityCreator.setEntityFactory(entityFactory);
        
        //TEST   
//        EntityCreator.createEntity("bubblegum_rainbow", 1250, 2911);
//        EntityCreator.createEntity("tourist", 1000, 200);
//        EntityCreator.createEntity("hunter", 1000, 200);


        loadMap(mapFilename);
        mapRenderSystem.initialzeRenderer(map, "map_background", cameraSystem);
        playerStateSystem.initializeDeathBorders(map);
    }

    /**
     * 
     * @param filename
     *            filepath to the map that is to be loaded
     */
    private void loadMap(String filename) {
        // Map laden
        try {
            map = new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Map konnte nicht geladen werden: " + filename, ex);
        }

        // Wenn map geladen wurde
        if (map != null) {

            // Map parsen
            MapLoader[] mapLoaders = { new PhysicsLoader(), new EntityLoader() };
            for (MapLoader mapLoader : mapLoaders) {
                mapLoader.parseMap(map, this, engine);
            }
        }
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(caveLightsRenderSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(animationEventHandlerSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(nameSystem);
        engine.addSystem(keyBoardInputSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(hudRenderSystem);
        engine.addSystem(triggerSystem);
        engine.addSystem(bulletSystem);
        engine.addSystem(respawnSystem );
        engine.addSystem(soundSystem);
        engine.addSystem(hitPointSystem);
        engine.addSystem(enemyHandlingSystem);
        engine.addSystem(dummyEnemySystem);
        engine.addSystem(enemyVisionSystem );
        engine.addSystem(mapRenderSystem);
        engine.addSystem(effectsRenderSystem);
        engine.addSystem(splatterSystem);
        engine.addSystem(scoreBoardSystem);
        engine.addSystem(bubblegumSpitSystem);
        engine.addSystem(bubbleGlueSystem);
        engine.addSystem(playerStateSystem);
        engine.addSystem(updatePlatformPositionSystem);
        engine.addSystem(platformHandlingSystem);
        engine.addSystem(platformSystem);
        engine.addSystem(blockingGumSystem);
        engine.addSystem(deathAnimationSystem);
        
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(PlayerComponent.class, new PlayerContactListener());
        contactListener.addListener(BulletComponent.class, new BulletListener());
        contactListener.addListener(BubblegumSpitComponent.class, new BubblegumSpitListener());
        contactListener.addListener(PlayerComponent.class, new PlayerPlatformListener());
        contactListener.addListener(BlockingGumComponent.class, new BlockingGumListener());
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 24);
        // PhysixBodyDef bodyDef = new
        // PhysixBodyDef(BodyDef.BodyType.StaticBody,
        // physixSystem).position(410, 500)
        // .fixedRotation(false);
        // Body body = physixSystem.getWorld().createBody(bodyDef);
        // body.createFixture(new
        // PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(800,
        // 20));
        // PhysixUtil.createHollowCircle(physixSystem, 180, 180, 150, 30, 6);
        //
        // createTrigger(410, 600, 3200, 40, (Entity entity) -> {
        // engine.removeEntity(entity);
        // });
    }


    
    public void cheatWin() {
        GameOverEvent.emit(true, getNextMapFilename());
    }
    
    public void update(float delta) {
//        delta = 0;
        cameraSystem.bindCamera(); 
        engine.update(delta);
    }

    public void createTrigger(Action action,float x, float y, float width, float height, Consumer<Entity> consumer) {
        Entity entity = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        entity.add(modifyComponent);

        TriggerComponent triggerComponent = engine.createComponent(TriggerComponent.class);
        triggerComponent.consumer = consumer;
        triggerComponent.action = action;
        entity.add(triggerComponent);

        modifyComponent.schedule(() -> {
            PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.StaticBody, physixSystem).position(x, y);
            bodyComponent.init(bodyDef, physixSystem, entity);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).sensor(true).shapeBox(width, height);
            bodyComponent.createFixture(fixtureDef);
            entity.add(bodyComponent);
        });
        engine.addEntity(entity);
    }

    public ParticleEffect effect;


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 screenCoords = new Vector2(screenX, screenY);
        Vector2 worldCoords = CameraSystem.screenToWorldCoordinates(screenCoords);

        if (button == 0)
            EntityCreator.createEntity("circle", worldCoords.x, worldCoords.y);
        else
            EntityCreator.createEntity("box", worldCoords.x, worldCoords.y);
        return true;
    }

    public InputProcessor getInputProcessor() {
        return keyBoardInputSystem;
    }

    @Override
    public void onPauseGameEvent(GameStateType state) {
        if ( state != engineState )
        {
            ImmutableArray<EntitySystem> usedSys = engine.getSystems();

            
            for( EntitySystem sys : usedSys )
            {
                 sys.setProcessing(false);
            }
            
            switch( state )
            {
            case GAME_PLAYING:
                for( EntitySystem sys : usedSys )
                {
                     sys.setProcessing(true);
                }
                break;
            case GAME_PLAYER_FREEZE:
                for( EntitySystem sys : updateOnFreeze )
                {
                     sys.setProcessing(true);
                }
                break;
            case GAME_PAUSE:
                for( EntitySystem sys : updateOnPause )
                {
                     sys.setProcessing(true);
                }
                break;
            }
            if(Main.IS_RELEASE)
                physixDebugRenderSystem.setProcessing(false);
            engineState = state;
        }
    }

    public static boolean isInState(GameStateType state) {

        return state == engineState;
    }
    public String getMapFilename() {
        return map.getFilename();
    }

    public String getNextMapFilename() {
        return map.getProperty("next_map", null);
    }
}
