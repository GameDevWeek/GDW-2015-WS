package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.events.BubblegumSpitSpawnEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;

public class BubblegumUsingSystem extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    
    public BubblegumUsingSystem() {
        super(Family.all(PlayerComponent.class).get());

    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent input = ComponentMappers.input.get(entity);
        
        if ( input != null )
        {
            if ( input.spit ) 
            {
                input.spit = false;
                BubblegumSpitSpawnEvent.emit();
                
            }
        }
        
    }

}
