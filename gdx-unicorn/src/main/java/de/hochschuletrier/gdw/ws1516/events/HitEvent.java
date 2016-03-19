package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class HitEvent {
    
    public static interface Listener {
        void onHitEvent(Entity wasHit, Entity source, int value);
    }
    
    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();
    
    public static void emit(Entity entity, Entity source, int value) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onHitEvent(entity, source, value);
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
