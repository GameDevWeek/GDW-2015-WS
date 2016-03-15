package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent.Listener;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;

public class ScoreBoardEvent {
/**
 * Wich scoreattribute has to be increased
 * @author Tobi
 *
 */
    public static enum ScoreType{
        CHOCO_COIN,
        BUBBLE_GUM,
        KILLED_ENEMIE,
        KILLED_OBSTACLE,
        DEATH,
        HIT;               
    }
    
    public static interface Listener {
        void onScoreEvent(ScoreType type,int value);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(ScoreType type,int value) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onScoreEvent(type,value);
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
