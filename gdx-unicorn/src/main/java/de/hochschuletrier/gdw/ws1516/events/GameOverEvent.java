package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.gdx.utils.SnapshotArray;


public class GameOverEvent {
    public static interface Listener {

        void onGameOverEvent(boolean won, String mapToLoad);
    }
    
    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(boolean won, String mapToLoad) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onGameOverEvent(won, mapToLoad);
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
