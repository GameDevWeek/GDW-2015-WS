package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class StartPointComponent extends Component implements Poolable {

    public int x;
    public int y;
    @Override
    public void reset() {
        x = 0;
        y = 0;
        
    }

}
