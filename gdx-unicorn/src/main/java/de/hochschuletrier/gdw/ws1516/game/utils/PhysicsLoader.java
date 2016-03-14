package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1516.game.Game;

public class PhysicsLoader implements MapLoader {

	
    public PhysicsLoader(){
        
    }

    @Override
    public void parseMap(final TiledMap map, final Game game, final PooledEngine engine) {
        
        // Generate static world
        //TODO : test
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (final Layer layer, final TileInfo info) -> info.getBooleanProperty("blocked", false),
                (final Rectangle rect) -> addSolidRectangle(rect, tileWidth, tileHeight));
        
    }
    
    private void addSolidRectangle(final Rectangle rect, final int tileWidth, final int tileHeight) {
        
    }
	
	
}
