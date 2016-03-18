package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Segment;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.SplashEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class EndExplosionSystem extends EntitySystem implements GameOverEvent.Listener {

    private Entity playerRed, playerBlue;
    private Engine engine;
    private ImmutableArray<Entity> players;

    public EndExplosionSystem() {
        super();
    }

    public EndExplosionSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        GameOverEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        GameOverEvent.unregister(this);
    }

    @Override
    public void onGameOverEvent() {
        // initialize playerRed and playerBlue
        players = engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
        PlayerComponent playerComponent = ComponentMappers.player.get(players.first());
        if (playerComponent.color == PlayerColor.RED) {
            playerRed = players.first();
            playerBlue = players.get(1);
        } else {
            playerBlue = players.first();
            playerRed = players.get(1);
        }

        // make the segments explode one by one
        PlayerComponent player1,player2;
        Entity player1Entity,player2Entity;

        if(ComponentMappers.player.get(playerRed).segments.size() > ComponentMappers.player.get(playerBlue).segments.size()){
            player1 = ComponentMappers.player.get(playerRed);
            player1Entity = playerRed;
            player2 = ComponentMappers.player.get(playerBlue);
            player2Entity = playerBlue;
        } else {
            player1 = ComponentMappers.player.get(playerBlue);
            player1Entity = playerBlue;
            player2 = ComponentMappers.player.get(playerRed);
            player2Entity = playerRed;
        }


        while(!player2.segments.isEmpty()){
            Vector2 segment = player1.segments.getFirst();

            SplashEvent.emit(segment,player1.color);
            player1.segments.removeLast();

            segment = player2.segments.getFirst();

            SplashEvent.emit(segment,player2.color);
            player2.segments.removeLast();
        }
        SplashEvent.emit(ComponentMappers.position.get(player2Entity).pos,player2.color);
        engine.removeEntity(player2Entity);
        while (!player1.segments.isEmpty()){
            Vector2 segment = player1.segments.getFirst();

            SplashEvent.emit(segment,player1.color);
            player1.segments.removeLast();
        }
        SplashEvent.emit(ComponentMappers.position.get(player1Entity).pos,player1.color);
        engine.removeEntity(player1Entity);
    }
}
