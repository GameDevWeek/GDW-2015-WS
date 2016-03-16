package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem;

/**
 * zum bewegen der Gegener wird eines dieser Events aufgerufen
 * @author Tobi
 *
 */
public class EnemyActionEvent {
    public static interface Listener {
        void onEnemyActionEvent(Entity enemy,EnemyHandlingSystem.Action.Type action,float strength);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(Entity enemy,EnemyHandlingSystem.Action.Type action,float strength) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onEnemyActionEvent(enemy,action,strength);
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
