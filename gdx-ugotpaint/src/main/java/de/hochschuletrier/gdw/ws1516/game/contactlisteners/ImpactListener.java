package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1516.events.PickupEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;

public class ImpactListener extends PhysixContactAdapter {
    private final PooledEngine engine;
    
    public ImpactListener(PooledEngine engine) {
        this.engine = engine;
    }

    @Override
    public void beginContact(PhysixContact contact) {
        Entity other = contact.getOtherComponent().getEntity();
        if(other != null) {
            if(ComponentMappers.pickup.has(other)) {
                PickupEvent.emit(contact.getMyComponent().getEntity(), other);
                engine.removeEntity(other);
            }
        }
    }

}
