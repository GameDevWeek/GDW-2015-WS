package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.JumpEvent.Listener;

public class MovementEvent {    public static interface Listener {
    void onMovementEvent(Entity entity,float dirX);
}

private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

public static void emit(Entity entity,float dirX) {
    Object[] items = listeners.begin();
    for (int i = 0, n = listeners.size; i < n; i++) {
        ((Listener) items[i]).onMovementEvent(entity,dirX);
    }
    listeners.end();
}

public static void register(Listener listener) {
    listeners.add(listener);
}

public static void unregister(Listener listener) {
    listeners.removeValue(listener, true);
}

public static void unregisterAll() {
    listeners.clear();
}

}