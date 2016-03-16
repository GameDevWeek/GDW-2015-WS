package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class FinalScoreEvent {
    public static interface Listener {
        void onFinalScoreChanged(long score);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(long score) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onFinalScoreChanged(score);
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