package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;
import de.hochschuletrier.gdw.ws1516.events.RainbowEvent;

public class PlayerStateSystem extends IteratingSystem implements RainbowEvent.Listener{

    public PlayerStateSystem() {
        super(Family.all(PlayerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComp=ComponentMappers.player.get(entity);
        playerComp.stateTimer=Math.max(playerComp.stateTimer-deltaTime, 0);
        if (playerComp.stateTimer<=0.0f){
            playerComp.state=State.NORMAL;
        }
    }

    @Override
    public void onRainbowCollect() {
        if (getEntities().size()>0){
            PlayerComponent playerComp = ComponentMappers.player.get(getEntities().get(0));
            playerComp.state=State.RAINBOW;
            playerComp.stateTimer=GameConstants.RAINBOW_MODE_TIME;
        }
    }
    
}
