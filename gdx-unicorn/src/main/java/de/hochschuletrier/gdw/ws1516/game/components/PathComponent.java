package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PathComponent extends Component implements Pool.Poolable {

    public ArrayList<Vector2> points = new ArrayList<>();
    
    @Override
    public void reset() {
        points.clear();
    }

}
