package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class RainbowEvent  {
    public static interface Listener {
        void onRainbowCollect(Entity player);
        void onRainbowModeEnd(Entity player);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void start(Entity player) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onRainbowCollect(player);
        }
        listeners.end();
    }
    public static void end(Entity player) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onRainbowModeEnd(player);
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
