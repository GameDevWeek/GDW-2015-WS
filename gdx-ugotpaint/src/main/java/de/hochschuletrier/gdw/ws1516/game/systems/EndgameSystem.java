package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.systems.IntervalSystem;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.EndgameEvent;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.SetCountdownEvent;
import de.hochschuletrier.gdw.ws1516.game.utils.Canvas;

public class EndgameSystem extends IntervalSystem implements EndgameEvent.Listener, GameOverEvent.Listener {

    private final Canvas canvas = Main.getCanvas();

    private PickupSystem pickupSystem;

    private boolean endGame;

    public EndgameSystem(int priority) {
        super(0.5f, 0);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.pickupSystem = engine.getSystem(PickupSystem.class);
        EndgameEvent.register(this);
        GameOverEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        EndgameEvent.unregister(this);
        GameOverEvent.unregister(this);
    }

    @Override
    protected void updateInterval() {
        canvas.updatePctFilled();
        float filled = canvas.getPctFilled();
        if(filled > 0.8f) {
            SetCountdownEvent.emit(10);
        }

        // on interval spawn a pickup during endgame
        if(endGame)
            pickupSystem.createRandomPickup();
    }

    @Override
    public void onEndgameEvent() {
        this.endGame = true;
    }

    @Override
    public void onGameOverEvent() {
        endGame= false;
    }
}
