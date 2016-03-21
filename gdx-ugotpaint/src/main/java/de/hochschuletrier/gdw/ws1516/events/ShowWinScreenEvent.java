package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

/**
 * Created by glumbatsch on 20.03.2016.
 */
public class ShowWinScreenEvent {
    public static interface Listener {
        void onShowWinScreenEvent(PlayerColor color, float pctWinnerFilled);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(PlayerColor color, float pctWinnerFilled) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onShowWinScreenEvent(color,pctWinnerFilled);
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
