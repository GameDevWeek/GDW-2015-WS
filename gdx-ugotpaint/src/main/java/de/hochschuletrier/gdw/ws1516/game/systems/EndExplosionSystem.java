package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.ShowWinScreenEvent;
import de.hochschuletrier.gdw.ws1516.events.SplashEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.PickupComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.SplashComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

/**
 * Created by glumbatsch on 18.03.2016.
 */
public class EndExplosionSystem extends IntervalSystem implements GameOverEvent.Listener {

    private Engine engine;
    private boolean gameOver = false;

    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> pickups;
    private float pctWinnerFilled;
    private ImmutableArray<Entity> splash;
    private PlayerColor winnerColor;

    public EndExplosionSystem(int priority) {
        super(GameConstants.END_EXPLOSION_INTERVAL, priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        GameOverEvent.register(this);
        pickups = engine.getEntitiesFor(Family.all(PickupComponent.class).get());
        splash = engine.getEntitiesFor(Family.all(SplashComponent.class).get());
        players = engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        GameOverEvent.unregister(this);
    }

    @Override
    public void onGameOverEvent() {
        final float redPct = Main.getCanvas().getRedPct();
        final float bluePct = Main.getCanvas().getBluePct();

        if (redPct > bluePct) {
            // red player wins -> blue explodes
            winnerColor = PlayerColor.RED;
            pctWinnerFilled = Main.getCanvas().getRedPct();
        } else if (redPct < bluePct) {
            // blue player wins -> red explodes
            winnerColor = PlayerColor.BLUE;
            pctWinnerFilled = bluePct;
        } else {
            winnerColor = PlayerColor.NEUTRAL;
            pctWinnerFilled = bluePct;
        }

        gameOver = true;
    }


    private Entity getLoserEntity() {
        if(winnerColor != PlayerColor.NEUTRAL) {
            for (Entity entity : players) {
                PlayerComponent player = ComponentMappers.player.get(entity);
                if(player.color != winnerColor)
                    return entity;
            }
        }
        return null;
    }
    
    @Override
    protected void updateInterval() {
        if (!gameOver) {
            return;
        }
        boolean done = true;
        Entity loserEntity = getLoserEntity();
        if(loserEntity != null) {
            PlayerComponent loser = ComponentMappers.player.get(loserEntity);
            if (!loser.segments.isEmpty()) {
                // kill loser segment
                splash(loser.segments.removeLast(), loser.color);
                done = false;
            }
            if (loser.segments.isEmpty()) {
                // kill loser entity
                PositionComponent pos = ComponentMappers.position.get(loserEntity);
                splash(pos.pos, loser.color);
                engine.removeEntity(loserEntity);
                done = true;
            }
        }
            
        if (pickups.size() > 0) {
            final Entity entity = pickups.first();
            engine.removeEntity(entity);
            Vector2 pos = ComponentMappers.position.get(entity).pos;
            SplashEvent.emit(pos, PlayerColor.NEUTRAL);
            if(done)
                done = splash.size() == 0;
        }
        // show win screen
        if (done) {
            if(players.size() > 0)
                engine.removeEntity(players.first());
            gameOver = false;
            ShowWinScreenEvent.emit(winnerColor, pctWinnerFilled);
        }
    }

    private void splash(final Vector2 point, final PlayerColor color) {
        SplashEvent.emit(point, color);
    }
}
