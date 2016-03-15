package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ws1516.events.TestEvent.Listener;

public class SoundEvent {
    public static interface Listener {
        void onSoundPlay(String sound, Entity playOver, boolean b);

        void onSoundStop(String sound, Entity playOver);
        void onSoundStop(Entity playOver);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    /**
     * emits a sound at the entity's position
     * 
     * @param sound
     *            name of the sound to play
     * @param entity
     *              entity to emit the sound
     * @param loops
     *          true if the sound is endlessly looping
     */
    public static void emit(String sound, Entity entity, boolean loops) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSoundPlay(sound, entity, loops);
        }
        listeners.end();
    }

    public static void emit(String sound, Entity entity) {
        emit(sound, entity, false);
    }

    public static void stopSound(Entity entity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSoundStop(entity);
        }
        listeners.end();
    }
    public static void stopSound(String sound,Entity entity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSoundStop(sound,entity);
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
