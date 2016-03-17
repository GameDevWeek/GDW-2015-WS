package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class HornAttackEvent {
    public static interface Listener {
        void onHornAttackStart(Entity player);
        void onHornAttackStop(Entity player);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void start(Entity player) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onHornAttackStart(player);
        }
        listeners.end();
    }
    public static void stop(Entity player) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onHornAttackStop(player);
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
