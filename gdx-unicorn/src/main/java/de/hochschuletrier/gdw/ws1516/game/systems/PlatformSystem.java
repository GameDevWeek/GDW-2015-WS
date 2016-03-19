package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.MovementEvent;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlatformComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.enemyStates.FollowPathEnemyState;

public class PlatformSystem extends IteratingSystem{
    
    private static final Logger logger = LoggerFactory.getLogger(PlatformSystem.class);
    
    protected float timePassed;
    private boolean increasing = true;
    
    
    
    public PlatformSystem() {
        super(Family.all(PlatformComponent.class, PathComponent.class, PhysixBodyComponent.class).get());
        
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PlatformComponent platformComponent = entity.getComponent(PlatformComponent.class);
        PathComponent pathComponent = entity.getComponent(PathComponent.class);
        PhysixBodyComponent bodyComponent = entity.getComponent(PhysixBodyComponent.class);
        
        if (pathComponent.points.size()>0){
            
            /// index für den Punkt raszufinden der von 0 - max -  0 pendelt
            int pathIndex;
            int maxIndex = pathComponent.points.size()*2 -2;
            
            if (platformComponent.pathIndex <pathComponent.points.size())
            {
                pathIndex = platformComponent.pathIndex;
            }else
            {
                pathIndex = pathComponent.points.size()-2-platformComponent.pathIndex%pathComponent.points.size();
            }
            
            Vector2 position = new Vector2(platformComponent.positionX, platformComponent.positionY);
            Vector2 nextTarget = pathComponent.points.get(pathIndex);
            
            
            if (nextTarget.x < position.x +10) {
                bodyComponent.setLinearVelocityX(-1.0f * GameConstants.PLATFORM_SPEED);
            } 
            else if (nextTarget.x > position.x -10) {
                bodyComponent.setLinearVelocityX(1.0f * GameConstants.PLATFORM_SPEED);
                logger.debug("TargetX {}", nextTarget.x);
                logger.debug("PositionX {}", position.x);
            }
            
            if (nextTarget.y < position.y +10) {
                bodyComponent.setLinearVelocityY(-1.0f * GameConstants.PLATFORM_SPEED);
                logger.debug("TargetY {}", nextTarget.y);
                logger.debug("PositionY {}", position.x);
            } 
            else if (nextTarget.y > position.y -10) {
                bodyComponent.setLinearVelocityY(1.0f * GameConstants.PLATFORM_SPEED);
            }
            
            if(Math.abs(nextTarget.x-position.x) < 5)
            {
                bodyComponent.setLinearVelocityX(0);
            }
            if(Math.abs(nextTarget.y-position.y) < 5)
            {
                bodyComponent.setLinearVelocityY(0);
            }
            
            //if ( nextTarget.dst2(position.x,position.y) < 100 )
            if (Math.abs(nextTarget.x - position.x)< 12 & Math.abs(nextTarget.y - position.y)<12)
            {
                if(!platformComponent.loop)
                {
                    platformComponent.pathIndex++;
                    platformComponent.pathIndex %= maxIndex;
                }
                else
                {
                    if(platformComponent.pathIndex < maxIndex && increasing)
                    {
                        platformComponent.pathIndex++;
                    }
                    else if(platformComponent.pathIndex > 0 && !increasing)
                    {
                        platformComponent.pathIndex--;
                    }
                    else if(platformComponent.pathIndex >= maxIndex)
                    {
                        platformComponent.pathIndex--;
                        increasing = false;
                    }
                    else if(platformComponent.pathIndex <= 0)
                    {
                        platformComponent.pathIndex++;
                        increasing = true;
                    }
                }
                
            }
        }
        
    }

}
