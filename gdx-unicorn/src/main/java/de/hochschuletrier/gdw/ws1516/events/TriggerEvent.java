package de.hochschuletrier.gdw.ws1516.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;


public class TriggerEvent {
    public static enum Action{
        DEATH_ZONE("death_zone"),NONE(""),SPAWN_ZONE("spawn_zone"),WINING_ZONE("goal"),CAVE_EXIT("cave_exit"),CAVE_ENTER("cave_enter");
        private String identifier;
        Action(String s)
        {            
            identifier = s.toUpperCase();
        }
        public static Action toAction(String name)
        {
            name = name.toUpperCase();
//            return Action.valueOf(name);
            
            for( Action a : Action.values() )
            {
                if ( a.identifier.equals(name) )
                {
                    return a;
                }
            }
            return NONE;
        }
    }
    
    public static interface Listener {
        void onTriggerEvent(Action action,Entity triggeringEntity);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

    public static void emit(Action action,Entity triggeringEntity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onTriggerEvent(action, triggeringEntity);
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
