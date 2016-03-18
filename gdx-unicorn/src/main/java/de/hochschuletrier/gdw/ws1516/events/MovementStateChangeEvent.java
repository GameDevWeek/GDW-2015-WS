package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.MovementEvent.Listener;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;

public class MovementStateChangeEvent {
    public static interface Listener {
        void onMovementStateChangeEvent(Entity entity, MovementComponent.State oldState, MovementComponent.State newState);
    }
    
    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();
    
    public static void emit(Entity entity, MovementComponent.State oldState, MovementComponent.State newState) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onMovementStateChangeEvent(entity, oldState, newState);
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
