package de.hochschuletrier.gdw.ws1516.events;


import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.TestEvent.Listener;
import de.hochschuletrier.gdw.ws1516.game.Game;

public class PauseGameEvent {
    public static interface Listener {
        public void onPauseGameEvent(boolean pauseOn);

        public void onPauseChange() ;
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(boolean pauseOn) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onPauseGameEvent(pauseOn);
            Game.pauseGame(pauseOn);
        }
        listeners.end();
    }

    public static void change() {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onPauseChange();
            Game.switchPause();
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
