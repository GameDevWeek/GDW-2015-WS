package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.factories.EntityFactoryParam;

public class EntityCreator {

    private static EntityFactoryParam factoryParam = new EntityFactoryParam();

    private static EntityFactory<EntityFactoryParam> entityFactory;

    private static PooledEngine engine;

    // keine Instanzen erstellen
    private EntityCreator() {
    }

    public static void setEngine(PooledEngine e) {
        engine = e;
    }

    /** Basic Entities */
    public static Entity createEntity(String name, float x, float y) {
        factoryParam.x = x;
        factoryParam.y = y;
        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        
        return entity;
    }

    /** Master Entities */
    public static Entity createEntity(String name, String path, float x, float y, float speed, boolean loop,
            String action) {
        factoryParam.x = x;
        factoryParam.y = y;
        factoryParam.path = path;
        factoryParam.speed = speed;
        factoryParam.loop = loop;
        factoryParam.action = action;

        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    /** Movable Entities */
    public static Entity createEntity(String name, String path, float x, float y, float speed, boolean loop) {
        factoryParam.x = x;
        factoryParam.y = y;
        factoryParam.path = path;
        factoryParam.speed = speed;
        factoryParam.loop = loop;

        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    /** Enemy Entities */
    public static Entity createEntity(String name, String path, float x, float y) {
        factoryParam.x = x;
        factoryParam.y = y;
        factoryParam.path = path;

        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    /** Action Entities */
    public static Entity createEntity(String name, float x, float y, String action) {
        factoryParam.x = x;
        factoryParam.y = y;
        factoryParam.action = action;

        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    public static void setGame(Game game) {
        factoryParam.game = game;
    }

    public static void setEntityFactory(EntityFactory<EntityFactoryParam> eF) {
        entityFactory = eF;
    }

}
