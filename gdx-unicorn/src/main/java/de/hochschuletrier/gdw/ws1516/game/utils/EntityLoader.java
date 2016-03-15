package de.hochschuletrier.gdw.ws1516.game.utils;

import java.util.LinkedList;

import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1516.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityLoader implements MapLoader {
    
    private static final Logger logger = LoggerFactory.getLogger(EntityLoader.class);

    @Override
    public void parseMap(TiledMap map, Game game, PooledEngine engine) {
        LinkedList<String> names = new LinkedList();
        for (Layer layer : map.getLayers()) {
            if (layer.isObjectLayer()) {
                for (LayerObject obj : layer.getObjects()) {
                    String name = obj.getProperty("entity_type", null);
                    if (name != null) {
                        final String path = obj.getProperty("path", null);
                        final float x = obj.getX() + obj.getWidth() * 0.5f;
                        final float y = obj.getY() - obj.getHeight() * 0.5f;
                        final float speed = obj.getFloatProperty("speed", 0.f);
                        final boolean loop = obj.getBooleanProperty("loop", false);
                        EntityCreator.createEntity(name, path, x, y, speed, loop);
                        names.add(name);
                    }
                }
            }
        }

        
        // Test Print
        logger.info("{}", names);
    }

}
