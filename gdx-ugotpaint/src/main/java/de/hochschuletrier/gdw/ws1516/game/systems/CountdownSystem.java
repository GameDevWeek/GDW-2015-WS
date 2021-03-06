package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.EndgameEvent;
import de.hochschuletrier.gdw.ws1516.events.GameOverEvent;
import de.hochschuletrier.gdw.ws1516.events.SetCountdownEvent;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;

public class CountdownSystem extends EntitySystem implements SetCountdownEvent.Listener {

    static enum State {
        GAME,
        ENDGAME,
        GAMEOVER
    };

    private State state = State.GAME;
    private float timeLeft =  GameConstants.COUNTDOWN_TIME;
    private final BitmapFont font;
    private final Color color = Color.WHITE;

    public CountdownSystem(int priority) {
        super(priority);
        font = Main.getInstance().getAssetManager().getFont("midnight_48");
    }

    @Override
    public void addedToEngine(Engine engine) {
        SetCountdownEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        SetCountdownEvent.unregister(this);
    }

    @Override
    public void onSetCountdownEvent(float timeLeft) {
        if(this.timeLeft > timeLeft)
            this.timeLeft = timeLeft;
    }

    @Override
    public void update(float deltaTime) {
        if(timeLeft > 0) {
            timeLeft -= deltaTime;
            if (timeLeft <= GameConstants.ENDGAME_DURATION && state == State.GAME) {
                state = State.ENDGAME;
                Main.playMusic("endgame");
                SoundEvent.emit(null, "alarm");
                EndgameEvent.emit();
            }
            if(timeLeft <= 0) {
                Main.playMusic("normal");
                SoundEvent.emit(null, "gameover");
                state = State.GAMEOVER;
                GameOverEvent.emit();
                timeLeft = 0;
            }
        }

        font.setColor(color);
        String result;
        if(timeLeft == 0) {
            result = "Game Over";
        } else {
            int minutes = (int)Math.floor(timeLeft / 60);
            int seconds = (int)Math.floor(timeLeft - minutes*60);
            result = "0" + minutes + ":";
            if(seconds <= 9)
                result += "0";
            result += seconds;
        }

        BitmapFont.TextBounds bounds = font.getBounds(result);
        float x = (GameConstants.WINDOW_WIDTH - bounds.width) * 0.5f;
        font.draw(DrawUtil.batch, result, x, 2);
    }

    public State getState() {
        return state;
    }
}
