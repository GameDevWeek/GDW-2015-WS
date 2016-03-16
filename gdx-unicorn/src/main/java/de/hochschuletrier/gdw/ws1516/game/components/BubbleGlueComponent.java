package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BubbleGlueComponent extends Component implements Poolable {

    public Entity gluedEntity;
    public Vector2 gluedToPosition;
    public float timeRemaining;
    
    @Override
    public void reset() {
        
    }

}