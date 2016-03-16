package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.DeathEvent.Listener;

public class EndFlyEvent {
    public static interface Listener {
        void onEndFlyEvent(Entity entity);
    }
    
    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(Entity entity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onEndFlyEvent(entity);
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
