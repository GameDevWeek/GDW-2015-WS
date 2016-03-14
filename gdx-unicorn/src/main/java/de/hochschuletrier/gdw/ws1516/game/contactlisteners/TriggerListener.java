package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ws1516.sandbox.physikTest.PhysixTest;

public class TriggerListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TriggerListener.class);

    public void beginContact(PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        if (otherComponent != null) {
            TriggerComponent tc = ComponentMappers.trigger.get(contact.getMyComponent().getEntity());
            if (tc != null && tc.consumer != null) {
                tc.consumer.accept(otherComponent.getEntity());
            }
        }
    }
}
