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
    private boolean moveDirX = false;
    
    
    
    public PlatformSystem() {
        super(Family.all(PlatformComponent.class, PathComponent.class, PhysixBodyComponent.class).get());
        
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PlatformComponent platformComponent = entity.getComponent(PlatformComponent.class);
        PathComponent pathComponent = entity.getComponent(PathComponent.class);
        PhysixBodyComponent bodyComponent = entity.getComponent(PhysixBodyComponent.class);
        
        if (pathComponent.points.size()>0){
            
            int pathIndex;
            int maxIndex = pathComponent.points.size()*2 -2;
            
            if (platformComponent.pathIndex < pathComponent.points.size())
            {
                pathIndex = platformComponent.pathIndex;
            }else
            {
                pathIndex = pathComponent.points.size()-2-platformComponent.pathIndex%pathComponent.points.size();
            }
            
            Vector2 position = new Vector2(platformComponent.positionX, platformComponent.positionY);
            Vector2 nextTarget = pathComponent.points.get(pathIndex);
            
            
            if(Math.abs(pathComponent.points.get(0).x - pathComponent.points.get(1).x) < 10)
            {
                moveDirX = false;
            }
            if(Math.abs(pathComponent.points.get(0).y - pathComponent.points.get(1).y) < 10)
            {
                moveDirX = true;
            }
            
            
            if (moveDirX && nextTarget.x < position.x +10) {
                bodyComponent.setLinearVelocityX(-1.0f * GameConstants.PLATFORM_SPEED);
            } 
            else if (moveDirX && nextTarget.x > position.x -10) {
                bodyComponent.setLinearVelocityX(1.0f * GameConstants.PLATFORM_SPEED);
            }
            
            if (!moveDirX && nextTarget.y < position.y +10) {
                bodyComponent.setLinearVelocityY(-1.0f * GameConstants.PLATFORM_SPEED);
            } 
            else if (!moveDirX && nextTarget.y > position.y -10) {
                bodyComponent.setLinearVelocityY(1.0f * GameConstants.PLATFORM_SPEED);
            }
            
//            if(moveDirX && Math.abs(nextTarget.x-position.x) < 5)
//            {
//                bodyComponent.setLinearVelocityX(0);
//            }
//            if(!moveDirX && Math.abs(nextTarget.y-position.y) < 5)
//            {
//                bodyComponent.setLinearVelocityY(0);
//            }
            
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
