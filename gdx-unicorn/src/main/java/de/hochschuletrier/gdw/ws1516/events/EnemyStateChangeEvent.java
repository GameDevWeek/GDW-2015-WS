package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;

public class EnemyStateChangeEvent {
    public static interface Listener {
        void onEnemyStateChangeEvent(Entity entity, EnemyBehaviourComponent.Behaviour from, EnemyBehaviourComponent.Behaviour to);
    }
    
    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(Entity entity, EnemyBehaviourComponent.Behaviour from, EnemyBehaviourComponent.Behaviour to) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onEnemyStateChangeEvent(entity, from, to);
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
