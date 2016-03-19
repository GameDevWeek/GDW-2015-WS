package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class BubblegumGlueSpawnEvent {
    public static interface Listener {
        
        /**
         * Glues a enemy
         * @param gum the gum spit that caused the glue
         * @param gluedEntity the entity that was glued
         */
        void onSpawnGlue (final Entity gum, final Entity gluedEntity);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(final Entity gum, final Entity gluedEntity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSpawnGlue(gum, gluedEntity);
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