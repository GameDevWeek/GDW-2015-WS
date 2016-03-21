package de.hochschuletrier.gdw.ws1516.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
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
import de.hochschuletrier.gdw.ws1516.game.systems.EndgameSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.PickupSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.ShootSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.UpdatePlayerSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.UpdateProjectileSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.UpdateSoundEmitterSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.*;
import de.hochschuletrier.gdw.ws1516.game.systems.input.InputSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.input.KeyboardInputSystem;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;
import java.util.ArrayList;

public class Game extends InputAdapter {

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", Game.class);
    private final PickupSystem pickupSystem = new PickupSystem(this, GameConstants.PRIORITY_ANIMATIONS-1);
    private final SplashSystem splashSystem = new SplashSystem(this);
    private final PlayerDeathSystem playerDeathSystem = new PlayerDeathSystem(this);
    private final ParticleRenderSystem particleRenderSystem = new ParticleRenderSystem(this, GameConstants.PRIORITY_ANIMATIONS-1);
    private final EndExplosionSystem endExplosionSystem = new EndExplosionSystem(GameConstants.PRIORITY_ENDGAME);

    private final InputForwarder inputForwarder = new InputForwarder();

    public void dispose() {
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

    public void init(AssetManagerX assetManager) {
        addSystems();
        entityFactory.init(engine, assetManager);

        for (PlayerColor color : PlayerColor.values()) {
            final String colorKey = color.name().toLowerCase();
            color.animation = assetManager.getAnimation("segment_" + colorKey);
            color.splashAnimation = assetManager.getAnimation("splash_" + colorKey);
            color.particleEffectExplosion = assetManager.getParticleEffect("explosion_" + colorKey);
            if (color != PlayerColor.NEUTRAL) {
                color.projectileAnimation = assetManager.getAnimation("projectile_" + colorKey);
                color.particleEffectSplash = assetManager.getParticleEffect("splash_" + colorKey);
            }
        }

        inputForwarder.set(engine.getSystem(KeyboardInputSystem.class));
        float x = GameConstants.BOUND_LEFT + GameConstants.DEFAULT_SEGMENTS * GameConstants.SEGMENT_DISTANCE;
        float y = GameConstants.BOUND_TOP;
        createSnake(1, x, y, 1, 0, PlayerColor.BLUE);


        x = GameConstants.BOUND_RIGHT - GameConstants.DEFAULT_SEGMENTS * GameConstants.SEGMENT_DISTANCE;
        y = GameConstants.BOUND_BOTTOM;
        createSnake(0, x, y, -1, 0, PlayerColor.RED);

        pickupSystem.createRandomPickup();
        pickupSystem.createRandomPickup();
    }

    private void addSystems() {
        engine.addSystem(new ShootSystem(this, GameConstants.PRIORITY_SHOOT));
        engine.addSystem(new UpdatePlayerSystem(GameConstants.PRIORITY_PHYSIX));
        engine.addSystem(new UpdateProjectileSystem(GameConstants.PRIORITY_PHYSIX));
        engine.addSystem(new CollisionSystem(GameConstants.PRIORITY_PHYSIX + 1));
        engine.addSystem(new CanvasRenderSystem(GameConstants.PRIORITY_CANVAS));
        engine.addSystem(new AnimationRenderSystem(GameConstants.PRIORITY_ANIMATIONS));
        engine.addSystem(new FirstSegmentRenderSystem(GameConstants.PRIORITY_ANIMATIONS+1));
        engine.addSystem(new KeyboardInputSystem());
        engine.addSystem(new InputSystem());
        engine.addSystem(new UpdateSoundEmitterSystem());

        // pickup- and countdown system needs to be added before the endgame system
        engine.addSystem(pickupSystem);
        engine.addSystem(new CountdownSystem(GameConstants.PRIORITY_HUD));
        engine.addSystem(new EndgameSystem(GameConstants.PRIORITY_ENDGAME));

        engine.addSystem(splashSystem);
        engine.addSystem(playerDeathSystem);
        engine.addSystem(particleRenderSystem);
        engine.addSystem(endExplosionSystem);
    }

    public void update(float delta) {
        Main.getInstance().screenCamera.bind();
        engine.update(delta);
    }

    public Entity createSnake(int index, float x, float y, float xDir, float yDir, PlayerColor color) {
        Entity e = createEntity("snake", x, y, color);
        InputComponent input = ComponentMappers.input.get(e);
        input.index = index;
        input.lastMoveDirection.add(xDir, yDir);
        e.add(input);

        PlayerComponent player = ComponentMappers.player.get(e);
        player.color = color;
        player.path.add(new Vector2(-xDir, -yDir).nor().scl(100).add(x, y));
        for (int i = 1; i < GameConstants.DEFAULT_SEGMENTS; i++)
            player.segments.add(new Vector2());
        e.add(player);
        return e;
    }

    public Entity createEntity(String name, float x, float y, PlayerColor color) {
        factoryParam.x = x;
        factoryParam.y = y;
        factoryParam.playerColor = color;
        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    public InputProcessor getInputProcessor() {
        return inputForwarder;
    }
}
