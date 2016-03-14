package de.hochschuletrier.gdw.ws1516.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ws1516.game.contactlisteners.ImpactListener;
import de.hochschuletrier.gdw.ws1516.game.systems.AnimationRenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.CanvasRenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.input.InputSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.input.KeyboardInputSystem;

public class Game extends InputAdapter {

    private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1);

    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", Game.class);

    private InputForwarder inputForwarder = new InputForwarder();
    
    public Game() {
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE) {
            togglePhysixDebug.register();
        }
    }

    public void dispose() {
        togglePhysixDebug.unregister();
    }

    public void init(AssetManagerX assetManager) {
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));

        addSystems();
        addContactListeners();
        setupPhysixWorld();
        entityFactory.init(engine, assetManager);
        
        inputForwarder.set(engine.getSystem(KeyboardInputSystem.class));
        createSnake(0, 50, 50, Color.RED);
        createSnake(1, 850, 550, Color.BLUE);
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        
        engine.addSystem(new CanvasRenderSystem(GameConstants.PRIORITY_CANVAS));
        engine.addSystem(new AnimationRenderSystem(GameConstants.PRIORITY_ANIMATIONS));
        engine.addSystem(updatePositionSystem);
        engine.addSystem(new KeyboardInputSystem());
        engine.addSystem(new InputSystem());
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(PlayerComponent.class, new ImpactListener());
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 0);
    }

    public void update(float delta) {
        Main.getInstance().screenCamera.bind();
        engine.update(delta);
    }

    public Entity createSnake(int index, float x, float y, Color tint) {
        Entity e = createEntity("snake", x, y, tint);
        final InputComponent input = engine.createComponent(InputComponent.class);
        input.index = index;
        e.add(input);
        e.add(engine.createComponent(PlayerComponent.class));
        return e;
    }
    
    public Entity createEntity(String name, float x, float y, Color tint) {
        factoryParam.tint = tint;
        factoryParam.x = x;
        factoryParam.y = y;
        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    public InputProcessor getInputProcessor() {
        return inputForwarder;
    }
}
