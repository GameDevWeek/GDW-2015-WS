package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1516.events.BulletSpawnEvent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.PaparazziShootEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent.HitType;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent.EnemyType;
import de.hochschuletrier.gdw.ws1516.game.systems.BulletSystem;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;

public class AttackEnemyState extends EnemyBaseState {
    private static final Logger logger = LoggerFactory.getLogger(AttackEnemyState.class);

    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
        EnemyBehaviourComponent behaviour = ComponentMappers.enemyBehaviour.get(entity);
        PositionComponent enemyPosition = ComponentMappers.position.get(entity);
        PositionComponent playerPosition = ComponentMappers.position.get(player);
        EnemyTypeComponent type=ComponentMappers.enemyType.get(entity);
        behaviour.cooldown=behaviour.maxCooldown;
        if (type.type==EnemyType.HUNTER){
            int direction = GameConstants.HUNTER_BULLET_OFFSET;
            if (playerPosition.x<enemyPosition.x){
                direction=-direction;
            }
            BulletSpawnEvent.emit(enemyPosition.x+direction, enemyPosition.y,
                    playerPosition.x-enemyPosition.x, playerPosition.y-enemyPosition.y,
                    (source,target)->{HitEvent.emit(player, HitType.BULLET, 1);}, (source,target)->{}, (e)->{DeathEvent.emit(e);});
            return new FollowPathEnemyState();
        }else{        
            float distance = (float)Math.sqrt( Math.pow(enemyPosition.x-playerPosition.x, 2)+ Math.pow(enemyPosition.y-playerPosition.y, 2) );
            PaparazziShootEvent.emit(distance);
            return new FollowPlayerEnemyState();
        }
    }

}
