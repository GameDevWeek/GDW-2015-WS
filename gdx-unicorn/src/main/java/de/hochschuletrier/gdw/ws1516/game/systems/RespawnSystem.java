package de.hochschuletrier.gdw.ws1516.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1516.events.GameRestartEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;
import de.hochschuletrier.gdw.ws1516.sandbox.maptest.MapTest;

public class RespawnSystem extends EntitySystem implements GameRestartEvent.Listener, EntityListener {

    private static final Logger logger = LoggerFactory.getLogger(GameLogicTest.class);
    private Entity player;
    private Entity startPoint;
    
    @Override
    public void onGameRestartEvent() {
        PositionComponent position = ComponentMappers.position.get(player);
        StartPointComponent playerStartPoint = ComponentMappers.startPoint.get(startPoint);
        if ( position != null && playerStartPoint != null )
        {
            position.x = playerStartPoint.x;
            position.y = playerStartPoint.y;
        }else
        {
            logger.warn("No Player or no RespawnPoint set");
        }
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        GameRestartEvent.unregister(this);
        engine.removeEntityListener(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.one(PlayerComponent.class, StartPointComponent.class).get();
        engine.addEntityListener(this);
        GameRestartEvent.register(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        if (entity.getComponent(PlayerComponent.class) != null) {
            player = entity;
        }
        if (entity.getComponent(StartPointComponent.class) != null) {
            startPoint = entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (entity == player) {
            player = null;
        } else if (entity == startPoint) {
            startPoint = null;
        }
    }
}
