package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent.Listener;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;

public class ThrowBackEvent {
    
    public static interface Listener {
        void onThrowBackEventStart(Entity unicorn, float dirX);
        void onThrowBackEventStop(Entity unicorn);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void start(Entity unicorn, float dirX) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onThrowBackEventStart(unicorn, dirX);
        }
        listeners.end();
    }
    public static void stop(Entity unicorn) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onThrowBackEventStop(unicorn);
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
