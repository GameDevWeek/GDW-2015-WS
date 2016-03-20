package de.hochschuletrier.gdw.ws1516.game.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.LightComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class CaveLightsRenderSystem extends IteratingSystem {

    private static boolean lightListComplete;
    
    private static List<Vector2> lights;
    
    @SuppressWarnings("unchecked")
    public CaveLightsRenderSystem(int priority) {
        super(Family.all(LightComponent.class, PositionComponent.class).get(), priority);
        
        lightListComplete = false;
        lights = new ArrayList<>();
    }
        
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (!lightListComplete)
        {
            PositionComponent pos = ComponentMappers.position.get(entity);
            if (pos != null && isInList(pos.x, pos.y))
            {
                lightListComplete = true;
            }
            else
            {
                lights.add(new Vector2(pos.x, pos.y));
            }
        }
    }
    
    private boolean isInList(float lightX, float lightY)
    {
        for (int i = 0; i < lights.size(); ++i)
        {
            if (lights.get(i).epsilonEquals(lightX, lightY, 1.0f))
            {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isInViewport(int lightIndex)
    {
        // axis aligned bounding box check
        return ( (lights.get(lightIndex).x + GameConstants.CAVE_LIGHT_GLOW_RADIUS) >= CameraSystem.getViewportTopLeft().x )
                && ( (lights.get(lightIndex).x - GameConstants.CAVE_LIGHT_GLOW_RADIUS) <= CameraSystem.getViewportBottomRight().x )
                && ( (lights.get(lightIndex).y + GameConstants.CAVE_LIGHT_GLOW_RADIUS) >= CameraSystem.getViewportTopLeft().y )
                && ( (lights.get(lightIndex).y - GameConstants.CAVE_LIGHT_GLOW_RADIUS) <= CameraSystem.getViewportBottomRight().y );
    }
    
    public static float[] getMapLightsInViewport(int maximumLights)
    {
        float[] lightSourceCoords  = new float[1 + 2 * maximumLights];
        int lightCoordIndex = 1; // index 0 stores number of lights
        if (lights != null) {
            for (int i = 0; i < lights.size(); ++i)
            {
                if ((lightCoordIndex - 1) / 2 >= maximumLights)
                {
                    break; // maximum number of lights reached
                }
                if (isInViewport(i))
                {
                    lightSourceCoords[lightCoordIndex++] = lights.get(i).x;
                    lightSourceCoords[lightCoordIndex++] = lights.get(i).y;
                }
            }
        }
        lightSourceCoords[0] = (lightCoordIndex - 1) / 2;
        return lightSourceCoords;
    }

}
