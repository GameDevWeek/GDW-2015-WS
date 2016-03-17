package de.hochschuletrier.gdw.ws1516.game.utils;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.Point;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.NameComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;

public class EntityLoader implements MapLoader {

    private static final Logger logger = LoggerFactory.getLogger(EntityLoader.class);

    @Override
    public void parseMap(TiledMap map, Game game, PooledEngine engine) {
        LinkedList<String> names = new LinkedList();
        for (Layer layer : map.getLayers()) {
            if (layer.isObjectLayer()) {
                for (LayerObject obj : layer.getObjects()) {
                    // only load tiles and rectangles
                    LayerObject.Primitive primitive = obj.getPrimitive();
                    if (primitive == LayerObject.Primitive.TILE || primitive == LayerObject.Primitive.POLYLINE) {
                        addEntity(engine, names, obj);
                    }
                }
            }
        }

        // Test Print
        logger.info("{}", names);
    }

    private void addEntity(PooledEngine engine, LinkedList<String> names, LayerObject obj) {
        String entity_type = obj.getProperty("entity_type", null);

        if (entity_type != null) {
            final String name = obj.getProperty("name", null);
            final float x = obj.getX() + obj.getWidth() * 0.5f;
            final float y = obj.getY() - obj.getHeight() * 0.5f;
            final float speed = obj.getFloatProperty("speed", 0.f);
            final boolean loop = obj.getBooleanProperty("loop", false);
            final String path = obj.getProperty("path",null);
            Entity e = EntityCreator.createEntity(entity_type, name, x, y, speed, loop);
            NameComponent nc = engine.createComponent(NameComponent.class);
            nc.name = name;
            e.add(nc);
            if (entity_type.equals("path")) {
                PathComponent pc = ComponentMappers.path.get(e);
                for (Point p : obj.getPoints()) {
                    pc.points.add(new Vector2(p.x, p.y));
                }
                logger.info("{}", e.getComponent(PathComponent.class).points);
            }
            names.add(entity_type);
        }
    }

}
