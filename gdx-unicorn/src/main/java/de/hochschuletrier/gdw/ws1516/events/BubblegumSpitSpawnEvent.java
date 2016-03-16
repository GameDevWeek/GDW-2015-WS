package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Spits a bubble-gum
 * @author Eileen
 * @version 1.0
 */
public class BubblegumSpitSpawnEvent {
    public static interface Listener {
        /**
         * Spits forth a bit of bubble-gum
         * @param onEnemyHit called when a entity has been hit (bullet, entity)
         * @param onHit called when something has been hit (bullet)
         */
        void onSpawnBubblegumSpit(float force);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(float force) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSpawnBubblegumSpit(force);
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
