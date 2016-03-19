package de.hochschuletrier.gdw.ws1516.game.utils;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyVisionSystem;

/**
 * Physics utility class to test whether or not a player entity can be seen from another entity
 * @author Eileen
 * @version 1.0
 */
public class PlayerRayCaster {

    private static final Logger logger = LoggerFactory.getLogger(PlayerRayCaster.class);
    
    //Context
    private Engine engine;
    private PhysixSystem physixSystem;
    private Entity toCastFrom;
    
    //Result
    private boolean lastCastResult;
    
    public PlayerRayCaster(Entity entity, Engine engine, PhysixSystem physixSystem) {
        this.toCastFrom = entity;
        this.engine = engine;
        this.physixSystem = physixSystem;
    }
    
    /**
     * Checks whether or not the given entity can see a player entity
     * @param entity the entity to check
     * @param engine the engine to get the entity's from
     * @param physixSystem physics environment
     */
    public void isPlayerVisible() {
        
        //Rest raycast result
        lastCastResult = true;
        
        //Fetch entity position component, if none abort
        PositionComponent entityPosition = ComponentMappers.position.get(toCastFrom);
        if (entityPosition == null) 
            return;
        
        //Fetch entity eye position (ray start)
        Vector2 entityEye = new Vector2(entityPosition.x, entityPosition.y);
        
        //For every player entity
        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                                                                          PhysixBodyComponent.class).get());
        
        ImmutableArray<Entity> enemies = engine.getEntitiesFor(Family.all(EnemyTypeComponent.class, PhysixBodyComponent.class).get());
        ArrayList<Body> enemyBodies = new ArrayList<>();
        enemies.forEach((c) -> enemyBodies.add(c.getComponent(PhysixBodyComponent.class).getBody()));

        for (Entity player : players) {
            PhysixBodyComponent playerBody = ComponentMappers.physixBody.get(player);
            Vector2 playerCenter = new Vector2(playerBody.getX(), playerBody.getY());
            
            Vector2 box2DEntityEye = new Vector2();
            physixSystem.toBox2D(entityEye, box2DEntityEye);
            Vector2 box2DPlayerCenter = new Vector2();
            physixSystem.toBox2D(playerCenter, box2DPlayerCenter);
            
            physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
                
                //Oclusion conditions
                if (fixture.getBody().getType().equals(BodyType.StaticBody) || 
                    fixture.getBody().getType().equals(BodyType.KinematicBody)) {
                    lastCastResult = false;
                    return 0;
                }
                
                return 1;
            }, box2DEntityEye, box2DPlayerCenter);
            
        }
        
        return;
    }
    
    /**
     * Returns the result of the last raytrace
     * @return true if a player is visible
     */
    public boolean getRaycastResult() {
        return lastCastResult;
    }
    
}
