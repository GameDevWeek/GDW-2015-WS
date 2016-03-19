package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlatformComponent extends Component implements Poolable{

    public boolean loop;
    public int pathIndex;
    public float positionX;
    public float positionY;
    
    @Override
    public void reset() {
        loop = false;
        pathIndex = 0;
        positionX = 0;
        positionY = 0;
    }

}
