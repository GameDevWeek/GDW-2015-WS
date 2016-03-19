package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.LookDirection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhysixBodyComponentFactory extends
        ComponentFactory<EntityFactoryParam> {

    private static final Logger logger = LoggerFactory
            .getLogger(PhysixBodyComponentFactory.class);
    private PhysixSystem physixSystem;

    @Override
    public String getType() {
        return "PhysixBody";
    }

    @Override
    public void init(PooledEngine engine, AssetManagerX assetManager) {
        super.init(engine, assetManager);

        physixSystem = engine.getSystem(PhysixSystem.class);
    }

    @Override
    public void run(Entity entity, SafeProperties meta,
            SafeProperties properties, EntityFactoryParam param) {
        final PhysixModifierComponent modifyComponent = engine
                .createComponent(PhysixModifierComponent.class);
        modifyComponent.schedule(() -> {
            String type = properties.getString("type", "");
            switch (type) {
            case "circle":
                addCircle(param, entity, properties);
                break;
            case "box":
                addBox(param, entity, properties);
                break;
            case "player":
                addPlayer(param, entity, properties);
                break;
            case "enemy":
                addEnemy(param, entity, properties);
                break;
            case "platform":
                addPlatform(param, entity, properties);
                break;
            default:
                logger.error("Unknown type: {}", type);
                break;
            }
        });
        entity.add(modifyComponent);
    }

    
    
    private void addPlayer(EntityFactoryParam param, Entity entity,
            SafeProperties properties) {

        float width = 2*GameConstants.TILESIZE_X;
        float height = 1*GameConstants.TILESIZE_Y;
        
        PhysixBodyComponent playerBody = getBodyComponent(param, entity);
        PhysixBodyDef playerDef = new PhysixBodyDef(
                BodyDef.BodyType.DynamicBody, physixSystem)
                .position(param.x, param.y).fixedRotation(true)
                .linearDamping(1).angularDamping(1);

        playerBody.init(playerDef, physixSystem, entity);
        
     // Horn (sensor)
     PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0f)
                .shapeCircle(width * 0.04f, new Vector2(width * 0.43f, height * -0.4f)).sensor(true);
        fixtureDef.filter.groupIndex = GameConstants.PHYSIX_COLLISION_UNICORN;
		Fixture fixture = playerBody.createFixture(fixtureDef);
        fixture.setUserData("horn");
        
        // mainBody
           fixtureDef = new PhysixFixtureDef(physixSystem)
            .density(0.68f).friction(1.0f).restitution(0f)
            .shapeCircle(width * 0.25f, new Vector2(1, 0));
           fixtureDef.filter.groupIndex = GameConstants.PHYSIX_COLLISION_UNICORN;
            fixture = playerBody.createFixture(fixtureDef);
            fixture.setUserData("body");
            
        
        // jump contact (sensor)
        fixtureDef = new PhysixFixtureDef(physixSystem)
        .density(1).friction(0f).restitution(0f)
        .shapeCircle(width * 0.05f, new Vector2(0, height * 0.49f)).sensor(true);
        fixtureDef.filter.groupIndex = GameConstants.PHYSIX_COLLISION_UNICORN;
        fixture = playerBody.createFixture(fixtureDef);
        fixture.setUserData("foot");

        entity.add(playerBody);
    }
    

    
    private void addEnemy(EntityFactoryParam param, Entity entity,
            SafeProperties properties) {

        float width = 2*GameConstants.TILESIZE_X;
        float height = 1*GameConstants.TILESIZE_Y;
        
        PhysixBodyComponent playerBody = getBodyComponent(param, entity);
        PhysixBodyDef playerDef = new PhysixBodyDef(
                BodyDef.BodyType.DynamicBody, physixSystem)
                .position(param.x, param.y).fixedRotation(true)
                .linearDamping(1).angularDamping(1);

        playerBody.init(playerDef, physixSystem, entity);


    // head
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0f)
                .shapeCircle(width * 0.09f, new Vector2(width * 0.08f, height * -0.4f));
        Fixture fixture = playerBody.createFixture(fixtureDef);

     // body
       fixtureDef = new PhysixFixtureDef(physixSystem)
        .density(1f).friction(0f).restitution(0f)
        .shapeBox(width * 0.18f, height * 0.825f, new Vector2(width * 0.08f, height * -0.1f), 0);
        fixture = playerBody.createFixture(fixtureDef);
        fixture.setUserData("head");


    // foot
        fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0f).restitution(0f)
                .shapeCircle(width * 0.1f, new Vector2(width * 0.08f, height * 0.32f));
        fixture = playerBody.createFixture(fixtureDef);
        fixture.setUserData("body");
        
    // jump contact (seonsor)
        fixtureDef = new PhysixFixtureDef(physixSystem)

        .density(1).friction(0f).restitution(0f)
        .shapeCircle(width * 0.1f, new Vector2(width * 0.08f, height * 0.32f)).sensor(true);
        fixture = playerBody.createFixture(fixtureDef);
        fixture.setUserData("foot");

        entity.add(playerBody);
    }
    
    private void addPlatform(EntityFactoryParam param, Entity entity,
            SafeProperties properties) {
        
        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, physixSystem)
        .position(param.x, param.y).fixedRotation(false);
        bodyComponent.init(bodyDef, physixSystem, entity);
        
        float tilesWidth = properties.getFloat("sizeWidth", 1.0f);
        float tilesHeight = properties.getFloat("sizeHeight", 1.0f);
        
      
        PhysixFixtureDef fixtureDef = getFixtureDef(properties)
                                      .friction(100.0f)
                                      .shapeBox(GameConstants.TILESIZE_X * tilesWidth,GameConstants.TILESIZE_Y * tilesHeight);
        
        
        
        bodyComponent.createFixture(fixtureDef);
        
        logger.debug("{}", fixtureDef.friction);       
        
        
        entity.add(bodyComponent);
    }

    private void addCircle(EntityFactoryParam param, Entity entity,
            SafeProperties properties) {
        PhysixBodyComponent bodyComponent = getBodyComponent(param, entity);
        PhysixFixtureDef fixtureDef = getFixtureDef(properties).shapeCircle(
                properties.getFloat("size", 5));
        fixtureDef.isSensor = properties.getBoolean("isSensor",false);
        bodyComponent.createFixture(fixtureDef);
        bodyComponent.setGravityScale(properties.getFloat("gravity",1.0f));
        entity.add(bodyComponent);
        logger.debug("Circle body created");
    }

    private void addBox(EntityFactoryParam param, Entity entity,
            SafeProperties properties) {
        PhysixBodyComponent bodyComponent = getBodyComponent(param, entity);
        PhysixFixtureDef fixtureDef = getFixtureDef(properties).shapeBox(
                properties.getFloat("size", 5), properties.getFloat("size", 5));
        bodyComponent.createFixture(fixtureDef);
        bodyComponent.applyImpulse(0, 50000);
        entity.add(bodyComponent);
    }

    private PhysixBodyComponent getBodyComponent(EntityFactoryParam param,
            Entity entity) {
        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = getBodyDef(param);
        bodyComponent.init(bodyDef, physixSystem, entity);
        return bodyComponent;
    }

    private PhysixBodyDef getBodyDef(EntityFactoryParam param) {
        return new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem)
                .position(param.x, param.y).fixedRotation(false);
    }

    private PhysixFixtureDef getFixtureDef(SafeProperties properties) {
        return new PhysixFixtureDef(physixSystem)
                .density(properties.getFloat("density", 5))
                .friction(properties.getFloat("friction", 5))
                .restitution(properties.getFloat("restitution", 0));
    }
    
    public static void recreatePlayerFixturesForDirection(PhysixBodyComponent playerBody, PhysixSystem physixSystem, LookDirection direction) {
        Fixture horn = playerBody.getFixtureByUserData("horn");
        Fixture head = playerBody.getFixtureByUserData("head");
        
        while (playerBody.getFixtureList().contains(horn, false)) {
            playerBody.getBody().destroyFixture(horn);
        }
        
        while (playerBody.getFixtureList().contains(head, false)) {
            playerBody.getBody().destroyFixture(head);
        }
        
        float width = 2*GameConstants.TILESIZE_X;
        float height = 1*GameConstants.TILESIZE_Y;
        float dir;
        
        if (direction == LookDirection.LEFT) {
            dir = -1;
        } else {
            dir = 1;
        }
        
        // Horn (sensor)
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0f)
                .shapeCircle(width * 0.04f, new Vector2(dir * width * 0.43f, height * -0.4f)).sensor(true);
        fixtureDef.filter.groupIndex = GameConstants.PHYSIX_COLLISION_UNICORN;
        Fixture fixture = playerBody.createFixture(fixtureDef);
        fixture.setUserData("horn");
        
    }
}
