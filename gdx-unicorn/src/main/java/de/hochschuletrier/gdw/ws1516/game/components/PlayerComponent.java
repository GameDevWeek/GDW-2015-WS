package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerComponent extends Component implements Poolable {

    public boolean doRespawn;

    @Override
    public void reset() {
        doRespawn = false;
    }

}
