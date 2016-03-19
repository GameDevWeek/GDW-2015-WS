package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
/**
 * @author Tobi
 *
 */
public class EnemyHandlingSystem extends IteratingSystem implements EntityListener {


    public static class ActionData
    {
        /**
         * seconds since start of this step
         */        
        public float seconds;
    }
    /**
     * no longer needed ?? denke ich
     * @author Tobi
     *
     * @param <T>
     */
    public static abstract class Action<T extends ActionData>
    { 
        public static enum Type
        {
            /**
             * Jumps with given Strength as Force
             */
            JUMP,
            /**
             *  Move with strength as velocity
             */
            MOVE,
            /**
             * Shoots with strength as ?range? 
             * direction towards Enemy looking to
             */
            SHOOT,
            SHOOT_ABORT
        }
        public T  data;
        /**
         * 
         * @param d
         *      Grants Access to the PatternWide Data
         */
        public Action(T d)
        {
            data = d;
        }
        /**
         * Is called every update
         * @param entity
         *      the enemy that is about to move
         * @param unicorn
         *      the unicorn/player
         * @return
         *      next State to enter
         */
        protected abstract int doStep(Entity entity,Entity unicorn);   
        protected void initStep(){};
        /**
         * gets called on switching to this step
         */
        private final void init()
        {
            data.seconds = 0;
            initStep();
        }
        /**
         * Is called every update
         * @param entity
         *      the enemy that is about to move
         * @param unicorn
         *      the unicorn/player
         * @param deltaTime
         *      time that passed
         * @return
         *      next State to enter
         */
        private final int apply(Entity entity,Entity unicorn, float deltaTime)
        {
            data.seconds+=deltaTime;
            return doStep(entity,unicorn);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(EnemyHandlingSystem.class);
    private Entity unicorn;
    private NameSystem nameSystem;

    
    public EnemyHandlingSystem(NameSystem nameSys) {
        super(Family.all(EnemyBehaviourComponent.class,EnemyTypeComponent.class).get());
        nameSystem = nameSys;
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(Family.all(PlayerComponent.class).get(), this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyBehaviourComponent behaviour = ComponentMappers.enemyBehaviour.get(entity);
        EnemyTypeComponent enemy = ComponentMappers.enemyType.get(entity);
        PathComponent path = ComponentMappers.path.get(entity);
        
        if ( enemy != null && behaviour.pathID != null && (path.points == null || path.points.isEmpty() ))
        {
            Entity pathEntity = nameSystem.getEntityByName(behaviour.pathID);
            if ( pathEntity != null )
            {
                PathComponent foundPath = ComponentMappers.path.get(pathEntity);
                path.points = new ArrayList<>(foundPath.points);  
                
            }else
            {
                behaviour.pathID  = null;
            }
        }
        behaviour.currentState = behaviour.currentState.compute(entity, unicorn, deltaTime);
        
    }

    @Override
    public void entityAdded(Entity entity) {
        if ( ComponentMappers.player.has(entity) )
        {
            unicorn = entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if ( ComponentMappers.player.get(entity) != null)
        {
            unicorn = null;
        }
        
    }
    
}
