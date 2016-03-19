package de.hochschuletrier.gdw.ws1516.game.systems.enemyStates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.audio.Sound;

import de.hochschuletrier.gdw.ws1516.events.BulletSpawnEvent;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.events.HitEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.events.PaparazziShootEvent;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent.EnemyType;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.State;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class AttackEnemyState extends EnemyBaseState {
    private static final Logger logger = LoggerFactory.getLogger(AttackEnemyState.class);
    private boolean soundPlayed = false;
    private boolean shootAlready=false;

    @Override
    public EnemyBaseState _compute(Entity entity, Entity player, float deltaTime) {
        EnemyBehaviourComponent behaviour = ComponentMappers.enemyBehaviour.get(entity);
        PositionComponent enemyPosition = ComponentMappers.position.get(entity);
        PositionComponent playerPosition = ComponentMappers.position.get(player);
        EnemyTypeComponent type=ComponentMappers.enemyType.get(entity);
        behaviour.cooldown=behaviour.maxCooldown;
        if (type.type==EnemyType.HUNTER){
            if (!soundPlayed) {
                SoundEvent.emit("huntergun", entity);
                soundPlayed = true;
            }
            logger.info("{},{}",timePassed,behaviour.cooldown);
            if (timePassed<behaviour.cooldown){
                if (timePassed>behaviour.cooldown-GameConstants.TIME_TO_WAIT_TO_SHOOT){
                    if (!shootAlready){
                        shootAlready=true;
                        EnemyActionEvent.emit(entity, Type.SHOOT, 0.0f);
                        logger.info("attack");
                    }
                    return this;
                }else{
                    if (behaviour.canSeeUnicorn){
                        return this;
                    }else{
                        SoundEvent.stopSound("huntergun", entity);
                        return new FollowPathEnemyState();
                    }
                }
            }
            int direction = GameConstants.HUNTER_BULLET_OFFSET;
            if (playerPosition.x<enemyPosition.x){
                direction=-direction;
            }
            BulletSpawnEvent.emit(enemyPosition.x+direction, enemyPosition.y,
                    playerPosition.x-(enemyPosition.x+direction), playerPosition.y-enemyPosition.y,
                    (bullet,target)->{HitEvent.emit(target, bullet, 1);}, (source,target)->{}, (e)->{DeathEvent.emit(e);});
            return new FollowPathEnemyState();
        }else{        
            float distance = (float)Math.sqrt( Math.pow(enemyPosition.x-playerPosition.x, 2)+ Math.pow(enemyPosition.y-playerPosition.y, 2) );
            SoundEvent.emit("paparazzishoot", entity);
            EnemyActionEvent.emit(entity, Type.SHOOT, 0.0f);
            PaparazziShootEvent.emit(distance);
            return new FollowPlayerEnemyState();
        }
    }

}
