package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import java.util.LinkedList;

public class PlayerComponent extends Component implements Pool.Poolable {

    public final LinkedList<Vector2> segments = new LinkedList();
    public final LinkedList<Vector2> path = new LinkedList();
    
    @Override
    public void reset() {
        segments.clear();
        path.clear();
    }
}
