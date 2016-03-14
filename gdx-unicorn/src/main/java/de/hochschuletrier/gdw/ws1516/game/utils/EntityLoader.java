package de.hochschuletrier.gdw.ws1516.game.utils;

import java.util.LinkedList;

import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1516.game.Game;

public class EntityLoader implements MapLoader {

    private LinkedList<String> objects;

    public EntityLoader() {
        objects = new LinkedList();
    }

    @Override
    public void parseMap(TiledMap map, Game game, PooledEngine engine) {

        for (Layer layer : map.getLayers()) {
            if (layer.isObjectLayer()) {
                for (LayerObject obj : layer.getObjects()) {
                    String name = obj.getProperty("entity_type", null);
                    if (name != null) {
                        EntityCreator.createEntity(name, obj.getProperty("path", null), obj.getX(), obj.getY(),
                                obj.getFloatProperty("speed", 0.f), obj.getBooleanProperty("loop", false));
                        objects.add(name);
                    }
                }
            }
        }

        // Test Print
        for (String s : objects) {
            System.out.print(s + ", ");
            System.out.println();
        }

    }

}
