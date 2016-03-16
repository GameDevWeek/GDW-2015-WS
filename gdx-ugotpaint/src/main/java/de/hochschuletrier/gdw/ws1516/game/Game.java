package de.hochschuletrier.gdw.ws1516.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ws1516.game.systems.AnimationRenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.CanvasRenderSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.CollisionSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.PickupSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.ShootSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.input.InputSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.input.KeyboardInputSystem;

public class Game extends InputAdapter {

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1);

    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", Game.class);
    private final PickupSystem pickupSystem = new PickupSystem(this);

    private final InputForwarder inputForwarder = new InputForwarder();
    
    public void dispose() {
    }

    public void init(AssetManagerX assetManager) {
        addSystems();
        entityFactory.init(engine, assetManager);
        
        inputForwarder.set(engine.getSystem(KeyboardInputSystem.class));
        createSnake(0, 50, 50, Color.RED);
        createSnake(1, 850, 550, Color.BLUE);
        pickupSystem.schedulePickup();
        pickupSystem.schedulePickup();
    }

    private void addSystems() {
        engine.addSystem(new CanvasRenderSystem(GameConstants.PRIORITY_CANVAS));
        engine.addSystem(new AnimationRenderSystem(GameConstants.PRIORITY_ANIMATIONS));
        engine.addSystem(updatePositionSystem);
        engine.addSystem(new KeyboardInputSystem());
        engine.addSystem(new InputSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new ShootSystem(this));
        engine.addSystem(pickupSystem);
    }

    public void update(float delta) {
        Main.getInstance().screenCamera.bind();
        engine.update(delta);
    }

    public Entity createSnake(int index, float x, float y, Color tint) {
        Entity e = createEntity("snake", x, y, 0, 0, tint);
        final InputComponent input = engine.createComponent(InputComponent.class);
        input.index = index;
        e.add(input);
        
        final PlayerComponent player = engine.createComponent(PlayerComponent.class);
        player.path.add(new Vector2(x - 100, y));
        e.add(player);
        return e;
    }
    
    public Entity createEntity(String name, float x, float y, float velX, float velY, Color tint) {
        factoryParam.color = tint;
        factoryParam.x = x;
        factoryParam.y = y;
        factoryParam.velX = velX;
        factoryParam.velY = velY;
        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    public InputProcessor getInputProcessor() {
        return inputForwarder;
    }
}
