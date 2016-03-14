package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;

/**
 * Map loader to load the physic components from a map.
 * @author Eileen
 * @version 1.0
 */
public class PhysicsLoader implements MapLoader {

    /** Friction value for static geometry parsed by this class */
    public static final float PHYSIX_WORLD_FRICTION = 0.5f;    
    
    private PhysixSystem physixSystem;  //Primary physics system
    
    /**
     * Create a new Physics loader object
     */
    public PhysicsLoader(){
        
        //Initialize the physics system
        this.physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
                                             GameConstants.VELOCITY_ITERATIONS,
                                             GameConstants.POSITION_ITERATIONS,
                                             GameConstants.PRIORITY_PHYSIX);

    }

    @Override
    public void parseMap(final TiledMap map, final Game game, final PooledEngine engine) {
        
        //Generate static world
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (final Layer layer, final TileInfo info) -> info.getBooleanProperty("blocked", false),
                (final Rectangle rect) -> createSolidGeometry(rect, tileWidth, tileHeight));
        
        //Load trigger from map (trigger : entity_type==trigger && action!="")
        map.getLayers().stream()                                                                   
                       .filter((layer) -> layer.isObjectLayer())                                 
                       .map((layer) -> layer.getObjects())                                      
                       .flatMap((list) -> list.stream())                                            
                       .filter((object) -> object.getProperty("entity_type", "").equals("trigger")) 
                       .filter((object) -> !object.getProperty("action", "").equals(""))           
                       .forEach((trigger) -> createTrigger(trigger));                             
        
    }
    
    //Create a trigger from the given object
    private void createTrigger(final LayerObject triggerObject) {
        
        //TODO : Create trigger zone
        
    }
    
    //Generate a solid, which represents the given rectangle
    private void createSolidGeometry(final Rectangle rect, final int tileWidth, final int tileHeight) {
        
        //Size and position of the triangle are in 'tile-units' so we have to convert them back into world units
        float boxWorldWidth  = rect.width * tileWidth;
        float boxWorldHeight = rect.height * tileHeight;
        float boxWorldCenterX = rect.x * tileWidth  + boxWorldWidth / 2.0f;
        float boxWorldCenterY = rect.y * tileHeight + boxWorldHeight / 2.0f;
        
        //Create the static geometry
                    
        //Create body definition
        PhysixBodyDef geomBodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem)
                                       .position(boxWorldCenterX, boxWorldCenterY)
                                       .fixedRotation(false);
        
        //Create the body in the world
        Body geomBody = physixSystem.getWorld().createBody(geomBodyDef);
        
        //Add fixture to body
        geomBody.createFixture(new PhysixFixtureDef(physixSystem)
                                   .density(1.0f)
                                   .friction(PHYSIX_WORLD_FRICTION)
                                   .shapeBox(boxWorldWidth, boxWorldHeight));
                                   
    }
    
}
