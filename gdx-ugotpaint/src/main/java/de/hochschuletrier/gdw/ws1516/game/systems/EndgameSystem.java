package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.systems.IntervalSystem;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.utils.Canvas;

public class EndgameSystem extends IntervalSystem {

    private final Canvas canvas = Main.getCanvas();

    public EndgameSystem(int priority) {
        super(0.5f, 0);
    }

    @Override
    protected void updateInterval() {
        canvas.updatePctFilled();
        float filled = canvas.getPctFilled();
        if(filled > 0.8) {
            // todo
        }
    }
}
