package de.hochschuletrier.gdw.ws1516.game.utils;

import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixUtil {
    private static final Logger logger = LoggerFactory.getLogger(PhysixUtil.class);

    public static final float DEG2RAD = (float) (Math.PI / 180.0); 
    public static final float RAD2DEG = (float) (180.0 / Math.PI);
    
    public static void createHollowCircle(PhysixSystem system, float x, float y, float radius, int sides, float sideStrength) {
        float sideLength = (float) (2 * radius * Math.tan(Math.PI / (float) sides));
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, system).position(x, y);
        Body body = system.getWorld().createBody(bodyDef);

        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(system).restitution(0).friction(1);
        for (int i = 0; i < sides; i++) {
            float angle = (float) (2 * Math.PI / sides * i);
            Vector2 center = new Vector2((float) (radius * Math.cos(angle)), (float) (radius * Math.sin(angle)));
            fixtureDef.shapeBox(sideStrength, sideLength, center, angle);
            body.createFixture(fixtureDef);
        }
    }

    public static void createCapsuleBody(Body body, PhysixFixtureDef fixtureDef, float length, float radius) {
        Vector2 center = new Vector2(0, -(length + radius) * 0.5f);
        fixtureDef.shapeBox(radius * 2, length, center, 0);
        body.createFixture(fixtureDef);

        Vector2 pos = new Vector2(center.x, center.y - length * 0.5f);
        body.createFixture(fixtureDef.shapeCircle(radius, pos));
        pos.y += length;
        body.createFixture(fixtureDef.shapeCircle(radius, pos));
    }
    
    /**
     * Throw the entity in the given direction
     * @param entity the entity to throw
     * @param forceX the force direction x component
     * @param forceY the force direction y component
     * @param forceScale how strong the force shall be
     */
    public static void throwBackEntity(Entity entity, float forceX, float forceY, float forceScale) {
        
        PhysixBodyComponent entityBody = ComponentMapper.getFor(PhysixBodyComponent.class).get(entity);
        if (entityBody != null) {
            float forceLen = (float) Math.sqrt(forceX * forceX + forceY * forceY);
            entityBody.applyImpulse((forceX / forceLen) * forceScale, (forceY / forceLen) * forceScale);
        }
        
    }
    
}
