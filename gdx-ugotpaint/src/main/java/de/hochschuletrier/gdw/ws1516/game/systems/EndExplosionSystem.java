package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.ShowWinScreenEvent;
import de.hochschuletrier.gdw.ws1516.events.SplashEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PickupComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class EndExplosionSystem extends IntervalSystem implements GameOverEvent.Listener {

    private Engine engine;
    private boolean gameOver = false;

    private PlayerComponent loser;
    private Entity loserEntity;
    private ImmutableArray<Entity> pickups;
    private PlayerComponent winner;

    public EndExplosionSystem(int priority) {
        super(GameConstants.END_EXPLOSION_INTERVAL, priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        GameOverEvent.register(this);
        this.pickups = engine.getEntitiesFor(Family.all(PickupComponent.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        GameOverEvent.unregister(this);
    }

    @Override
    public void onGameOverEvent() {
        // initialize playerRed and playerBlue
        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
        PlayerComponent playerComponent = ComponentMappers.player.get(players.first());
        Entity playerBlue;
        Entity playerRed;
        if (playerComponent.color == PlayerColor.RED) {
            playerRed = players.first();
            playerBlue = players.get(1);
        } else {
            playerBlue = players.first();
            playerRed = players.get(1);
        }

        if (Main.getCanvas().getRedPct() > Main.getCanvas().getBluePct()) {
            // red player wins -> blue explodes
            loser = ComponentMappers.player.get(playerBlue);
            winner = ComponentMappers.player.get(playerRed);
            loserEntity = playerBlue;
        } else {
            // blue player wins -> red explodes
            loser = ComponentMappers.player.get(playerRed);
            winner = ComponentMappers.player.get(playerBlue);
            loserEntity = playerRed;
        }

        gameOver = true;
    }


    @Override
    protected void updateInterval() {
        if (!gameOver) {
            return;
        }
        PlayerColor color = loser.color;
        if (!loser.segments.isEmpty()) {
            // kill loser segment
            splash(loser.segments.removeLast(), loser.color);
        } else if (loserEntity != null) {
            // kill loser entity
            PositionComponent pos = ComponentMappers.position.get(loserEntity);
            splash(pos.pos, color);
            engine.removeEntity(loserEntity);
            gameOver = false;
            // show win screen
            if (winner.color == PlayerColor.RED){
                ShowWinScreenEvent.emit("Spieler rot");
            } else {
                ShowWinScreenEvent.emit("Spieler blau");
            }
        }
        if (pickups.size() > 0) {
            final Entity entity = pickups.first();
            engine.removeEntity(entity);
            Vector2 pos = ComponentMappers.position.get(entity).pos;
            SplashEvent.emit(pos, PlayerColor.NEUTRAL);
        }
    }

    private void splash(final Vector2 point, final PlayerColor color) {
        SplashEvent.emit(point, color);
    }
}
