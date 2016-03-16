package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.systems.CollisionSystem;
import java.util.Iterator;
import java.util.LinkedList;

public class PlayerComponent extends Component implements Pool.Poolable {

    public final LinkedList<Vector2> segments = new LinkedList();
    public final LinkedList<Vector2> path = new LinkedList();
    
    @Override
    public void reset() {
        segments.clear();
        path.clear();
    }
    
    public Vector2 removeFirstSegments(int num) {
        num = Math.min(segments.size(), num);
        if(num <= 0)
            return null;

        // Reduce path
        Iterator<Vector2> it = path.iterator();
        float toRemove = GameConstants.SEGMENT_DISTANCE * num;
        if(it.hasNext()) {
            Vector2 last = it.next();
            Vector2 delta = new Vector2();
            while(it.hasNext()) {
                Vector2 v = it.next();
                float dist = last.dst(v);
                if(dist >= toRemove) {
                    if(dist == toRemove)
                        it.remove();
                    else {
                        delta.set(v).sub(last).nor().scl(toRemove);
                        last.add(delta);
                    }
                    break;
                } else {
                    toRemove -= dist;
                    last.set(v);
                    it.remove();
                }
            }
        }
        
        Vector2 result = null;
        while(num > 0) {
            result = segments.removeFirst();
            num--;
        }
        return result;
    }
}
