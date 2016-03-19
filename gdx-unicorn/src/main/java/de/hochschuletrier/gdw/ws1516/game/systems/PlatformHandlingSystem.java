package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlatformComponent;

public class PlatformHandlingSystem  extends IteratingSystem{

    
    private NameSystem nameSystem;
    
    public PlatformHandlingSystem(NameSystem nameystem) {
        super(Family.all(PlatformComponent.class).get());
        nameSystem=nameystem;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        
        PlatformComponent platform = ComponentMappers.platform.get(entity);
        PathComponent path = ComponentMappers.path.get(entity);
        
        // if ( enemy != null && behaviour.pathID != null && (path.points == null || path.points.isEmpty() ))
        
        if(platform.pathID != null &&(path.points == null || path.points.isEmpty()))
        {
            Entity pathEntity = nameSystem.getEntityByName(platform.pathID);
            if ( pathEntity != null )
            {
                PathComponent foundPath = ComponentMappers.path.get(pathEntity);
                path.points = new ArrayList<>(foundPath.points);  
                platform.positionX = path.points.get(0).x;
                platform.positionY = path.points.get(0).y;
                
            }else
            {
                platform.pathID  = null;
            }

        }
        
    }
    
    
    
    
    

}
