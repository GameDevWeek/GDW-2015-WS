package de.hochschuletrier.gdw.ws1516.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1516.events.PickupEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PickupComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;

public class PlayerImpactListener extends PhysixContactAdapter {
    private final PooledEngine engine;
    private final PhysixSystem system;
    
    public PlayerImpactListener(PooledEngine engine) {
        this.engine = engine;
        system = engine.getSystem(PhysixSystem.class);
    }

    @Override
    public void beginContact(PhysixContact contact) {
        Entity other = contact.getOtherComponent().getEntity();
        if(other != null) {
            if(ComponentMappers.pickup.has(other)) {
                final Entity self = contact.getMyComponent().getEntity();
                PlayerComponent player = ComponentMappers.player.get(self);
                InputComponent input = ComponentMappers.input.get(self);
                
                final InputComponent newInput = engine.createComponent(InputComponent.class);
                newInput.setFrom(input);
                other.add(newInput);
                final PlayerComponent newPlayer = engine.createComponent(PlayerComponent.class);
                newPlayer.setFrom(player);
                other.add(newPlayer);
                
                self.remove(InputComponent.class);
                self.remove(PlayerComponent.class);
                self.remove(PickupComponent.class);
                
                AnimationComponent animation = ComponentMappers.animation.get(self);
                AnimationComponent newAnimation = ComponentMappers.animation.get(other);
                newAnimation.tint = animation.tint;
                newAnimation.animation = animation.animation;
                
                PhysixModifierComponent modifier = ComponentMappers.physixModifier.get(self);
                modifier.schedule(()-> {
                    PhysixBodyComponent bodySelf = ComponentMappers.physixBody.get(self);
                    bodySelf.setLinearVelocity(0, 0);
                });
                
                PickupEvent.emit(self, other);
            }
        }
    }

}
