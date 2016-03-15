package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;

public class ProjectileImpactListener extends PhysixContactAdapter {
    private final PooledEngine engine;
    
    public ProjectileImpactListener(PooledEngine engine) {
        this.engine = engine;
    }

    @Override
    public void beginContact(PhysixContact contact) {
        Entity other = contact.getOtherComponent().getEntity();
        if(other != null) {
            final Entity self = contact.getMyComponent().getEntity();
            if(ComponentMappers.player.has(other)) {
                AnimationComponent anim1 = ComponentMappers.animation.get(other);
                AnimationComponent anim2 = ComponentMappers.animation.get(self);
                if(anim1.tint == anim2.tint)
                    return;
                //Fixme: kill other player
            }
            engine.removeEntity(self);
        }
    }

}
