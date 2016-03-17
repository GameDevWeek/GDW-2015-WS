package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class HornAttackEvent {
    public static interface Listener {
        void onHornAttackStart();
        void onHornAttackStop();
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void start() {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onHornAttackStart();
        }
        listeners.end();
    }
    public static void stop() {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onHornAttackStop();
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
