package de.hochschuletrier.gdw.ws1516.sandbox.physikTest;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
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
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ws1516.game.utils.PhysicsLoader;
import de.hochschuletrier.gdw.ws1516.sandbox.SandboxGame;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixTest extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(PhysixTest.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 30;
    public static final int BOX2D_SCALE = 40;
    
    private final float playerDensity=5f;
    private final float playerFriction=0.2f;
    private final float playerRestitution=0.4f;
    private final float playerSize=30;
    
    float time=0;

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

    public PhysixTest() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
    }

    @Override
    public void init(final AssetManagerX assetManager) {
        map = loadMap("data/maps/demo.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);


        //TODO : fix call with null
        PhysicsLoader loader = new PhysicsLoader();
        loader.parseMap(map, null, engine);
        
        physixSystem.setGravity(0, GRAVITY);
        
        // create a simple test player 
        Entity player = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        player.add(modifyComponent);

        modifyComponent.schedule(() -> {
            float width = 2*GameConstants.TILESIZE_X;
            float height = 1*GameConstants.TILESIZE_Y;
            
            PhysixBodyComponent playerBody = getBodyComponent(new Vector2(20,40), player);
            PhysixBodyDef playerDef = new PhysixBodyDef(
                    BodyDef.BodyType.DynamicBody, physixSystem)
                    .position(20, 40).fixedRotation(true)
                    .linearDamping(1).angularDamping(1);

            playerBody.init(playerDef, physixSystem, player);
            float scale = 20;
         // Horn
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                    .density(1).friction(0).restitution(0f)
                    .shapeCircle(width * 0.1f, new Vector2(0, -height * 0.4f));
            Fixture fixture = playerBody.createFixture(fixtureDef);

           fixtureDef = new PhysixFixtureDef(physixSystem)
            .density(1f).friction(0f).restitution(0f)
            .shapeBox(width * 0.18f, height * 0.825f, new Vector2(0, 0), 0);
            fixture = playerBody.createFixture(fixtureDef);

            /*fixtureDef = new PhysixFixtureDef(physixSystem)
            .density(1).friction(0).restitution(0f)
            .shapeCircle(width*0.4f,new Vector2(0,-height * 0.1f)).sensor(true);
            fixture = bodyComponent.createFixture(fixtureDef);*/


            //mainBody
            fixtureDef = new PhysixFixtureDef(physixSystem)
                    .density(1).friction(0f).restitution(0f)
                    .shapeCircle(width * 0.1f, new Vector2(0, height * 0.425f));
            fixture = playerBody.createFixture(fixtureDef);
            fixture.setUserData("laser");
            
            //jump contact
            fixtureDef = new PhysixFixtureDef(physixSystem)

            .density(1).friction(0f).restitution(0f)
            .shapeCircle(width * 0.08f, new Vector2(0, height * 0.49f)).sensor(true);
            fixture = playerBody.createFixture(fixtureDef);
            fixture.setUserData("jump");

            player.add(playerBody);
        });
        engine.addEntity(player);
      
        // Setup camera
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        totalMapWidth = map.getWidth() * map.getTileWidth();
        totalMapHeight = map.getHeight() * map.getTileHeight();
        camera.setBounds(0, 0, totalMapWidth, totalMapHeight);
        camera.updateForced();
        Main.getInstance().addScreenListener(camera);
    }
    
    private PhysixBodyComponent getBodyComponent(Vector2 param,
            Entity entity) {
        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = getBodyDef(param);
        bodyComponent.init(bodyDef, physixSystem, entity);
        return bodyComponent;
    }

    private PhysixBodyDef getBodyDef(Vector2 param) {
        return new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem)
                .position(param.x, param.y).fixedRotation(false);
    }
    @Override
    public void dispose() {
        Main.getInstance().removeScreenListener(camera);
        tilesetImages.values().forEach(Texture::dispose);
    }

    public TiledMap loadMap(final String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    @Override
    public void update(final float delta) {
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
            float cooldown=0.5f;

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

//            playerBody.setLinearVelocity(velX, velY);
//            playerBody.applyImpulse(velX, velY);
            playerBody.setLinearVelocityX(velX);
            
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)&&time<=0) {
                time=cooldown;
                playerBody.applyImpulse(0, -5000);
            }
            
            if(time>0){
                time-=delta;
            }
            camera.setDestination(playerBody.getPosition());
        }
    }
}

