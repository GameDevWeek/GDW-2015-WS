package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ws1516.events.TriggerEvent.Action;

import java.util.function.Consumer;

public class TriggerComponent extends Component implements Pool.Poolable {

    public Consumer<Entity> consumer;
    public Action action;

    @Override
    public void reset() {
        consumer = null;
        action = Action.NONE;
    }
}
