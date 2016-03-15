package de.hochschuletrier.gdw.ws1516.events;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class BulletSpawnEvent {
    public static interface Listener {
        /**
         * Spawns a bullet
         * @param spawnX the x world position of the bullet
         * @param spawnY the y world position of the bullet
         * @param directionX the x direction of the bullet
         * @param directionY the y direction of the bullet
         * @param onPlayerHit called when a player has been hit (bullet, player)
         * @param onEntityHit called when a entity has been hit (bullet, entity)
         * @param onHit called when something has been hit (bullet)
         */
        void onSpawnBullet (final float spawnX, final float spawnY, final float directionX, final float directionY, 
                            final BiConsumer<Entity, Entity> onPlayerHit,
                            final BiConsumer<Entity, Entity> onEntityHit,
                            final Consumer<Entity> onHit);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(final float spawnX, final float spawnY, final float directionX, final float directionY, 
                            final BiConsumer<Entity, Entity> onPlayerHit,
                            final BiConsumer<Entity, Entity> onEntityHit,
                            final Consumer<Entity> onHit) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSpawnBullet(spawnX, spawnY, directionX, directionY, onPlayerHit, onEntityHit, onHit);
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
