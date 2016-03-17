package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerComponent extends Component implements Poolable {

    public boolean doRespawn;
    public int maxHitpoints;
    public int hitpoints;
    public int lives;

    @Override
    public void reset() {
        doRespawn = false;
    }

}
