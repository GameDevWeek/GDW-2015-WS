package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

public class SplashEvent {
    public static interface Listener {
        void onSplashEvent(Vector2 first, PlayerColor color);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(Vector2 first, PlayerColor color) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSplashEvent(first, color);
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
