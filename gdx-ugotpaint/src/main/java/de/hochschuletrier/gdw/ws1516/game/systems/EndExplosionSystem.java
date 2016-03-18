package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Segment;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.SplashEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.Canvas;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class EndExplosionSystem extends IntervalSystem implements GameOverEvent.Listener {

    private Entity playerRed, playerBlue;
    private Engine engine;
    private ImmutableArray<Entity> players;

    private PlayerComponent player1, player2;
    Entity player1Entity, player2Entity;

    private Entity playerToBeDestroyed;
    private PlayerComponent playerComponentToBeDestroyed;

    public EndExplosionSystem() {
        super(0.5f, 0);
    }

    public EndExplosionSystem(int priority) {
        super(0.5f, priority);
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

        PlayerComponent playerComponentR = ComponentMappers.player.get(playerRed);
        PlayerComponent playerComponentB = ComponentMappers.player.get(playerBlue);
        if (playerComponentR.segments.size() > playerComponentB.segments.size()) {
            player1 = playerComponentR;
            player1Entity = playerRed;
            player2 = playerComponentB;
            player2Entity = playerBlue;
        } else {
            player1 = playerComponentB;
            player1Entity = playerBlue;
            player2 = playerComponentR;
            player2Entity = playerRed;
        }
    }


    @Override
    protected void updateInterval() {
        if(player1 == null){
            return;
        }
        if (!player1.segments.isEmpty() && player1.segments.size() > player2.segments.size()) {
            // kill player1 segment
            splash(player1.segments.removeLast(), player1.color);
        } else if (!player2.segments.isEmpty()) {
            // kill player 2 segment
            splash(player2.segments.removeLast(), player2.color);
        } else if (player1Entity != null) {
            // kill player1 entity
            PositionComponent pos = ComponentMappers.position.get(player1Entity);
            PlayerComponent play = ComponentMappers.player.get(player1Entity);
            splash(pos.pos, play.color);
            engine.removeEntity(player1Entity);
            player1Entity = null;
        } else if (player2Entity != null) {
            // kill player2 entity
            PositionComponent pos = ComponentMappers.position.get(player2Entity);
            PlayerComponent play = ComponentMappers.player.get(player2Entity);
            splash(pos.pos, play.color);
            engine.removeEntity(player2Entity);
            player2Entity = null;
        }
    }

    private void splash(final Vector2 point, final PlayerColor color) {
        SplashEvent.emit(point, color);
        Canvas canvas = Main.getCanvas();
        canvas.setColor(color.color);
        canvas.drawPoint(point, GameConstants.PAINT_RADIUS_BIG);
    }
}
