package de.hochschuletrier.gdw.ws1516.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.TestEvent.Listener;
import de.hochschuletrier.gdw.ws1516.game.Game;

public class PaparazziShootEvent {
    

    private static final Logger logger = LoggerFactory.getLogger(PaparazziShootEvent.class);
    
    public static interface Listener {
        void onPaparazziShootEvent(float distance);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(float distance) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onPaparazziShootEvent(distance);
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
