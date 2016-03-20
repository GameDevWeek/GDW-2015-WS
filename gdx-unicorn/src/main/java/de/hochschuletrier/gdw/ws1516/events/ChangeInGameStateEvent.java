package de.hochschuletrier.gdw.ws1516.events;


import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.TestEvent.Listener;
import de.hochschuletrier.gdw.ws1516.game.Game;

public class ChangeInGameStateEvent {
    public static interface Listener {
        public void onPauseGameEvent(GameStateType state);
    }
    public static enum GameStateType {
        GAME_PAUSE,
        GAME_PLAYING,
        GAME_PLAYER_FREEZE
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(GameStateType state) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onPauseGameEvent(state);
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
