package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.SandBoxEventLogger;

public class EnemyVisionSystem extends IteratingSystem implements EntityListener{

    private Entity unicorn = null;
    
    public EnemyVisionSystem() {
        super(Family.all(EnemyBehaviourComponent.class,PositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent enemyPosition=ComponentMappers.position.get(entity);
        EnemyBehaviourComponent enemyBehaviour=ComponentMappers.enemyBehaviour.get(entity);
        PositionComponent unicornPosition=ComponentMappers.position.get(unicorn);
        float xDist=enemyPosition.x-unicornPosition.x;
        float yDist=enemyPosition.y-unicornPosition.y;
        float dist=(float)Math.sqrt((xDist*xDist)+(yDist*yDist));
        if (dist<=GameConstants.GLOBAL_VISION*GameConstants.UNICORN_SIZE){
            enemyBehaviour.canSeeUnicorn=true;
        }else{
            enemyBehaviour.canSeeUnicorn=false;
        }
        enemyBehaviour.canFireRange= dist<=enemyBehaviour.shootingRange;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(this);
    }
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
    }
    @Override
    public void entityAdded(Entity entity) {
        if (entity.getComponent(PlayerComponent.class)!=null && entity.getComponent(PositionComponent.class)!=null){
            unicorn=entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (entity==unicorn){
            unicorn=null;
        }
    }

}
