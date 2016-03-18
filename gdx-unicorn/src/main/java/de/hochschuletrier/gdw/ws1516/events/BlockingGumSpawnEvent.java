package de.hochschuletrier.gdw.ws1516.events;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class BlockingGumSpawnEvent {
    public static interface Listener {
        /**
         * Spawns a bullet
         * @param spawnX the x world position of the bullet
         * @param spawnY the y world position of the bullet
         * @param angle the angle of the gum in degress
         * @param onDespawn callback to be called when the blocking gum despawns
         */
        void onSpawnBlockingGum(final float spawnX, final float spawnY, float angle,
                                final Consumer<Entity> onDespawn);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(final float spawnX, final float spawnY, float angle, final Consumer<Entity> onDespawn) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSpawnBlockingGum(spawnX, spawnY, angle, onDespawn);
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
