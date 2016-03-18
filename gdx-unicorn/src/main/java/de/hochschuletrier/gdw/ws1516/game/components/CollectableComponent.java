package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;

public class CollectableComponent extends Component {
    public static enum CollectableType {
        CHOCO_COIN,
        BONBON,
        RAINBOW_GUM,
        BLUE_GUM,
        PINK_GUM,
        SPAWN_POINT,
        NONE
    }
    
    public CollectableType type;
    public boolean isCollected;
    
    public void reset()
    {
        type = CollectableType.NONE;
        isCollected = false;
    }
    
}
