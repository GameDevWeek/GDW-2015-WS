package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class NameComponent extends Component implements Poolable{

    public String name;
    
    @Override
    public void reset() {
        name = null;
    }

}
