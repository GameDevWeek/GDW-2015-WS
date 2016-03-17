package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerComponent extends Component implements Poolable {

    public boolean doRespawn;
    public int maxHitpoints;
    public int hitpoints;
    public int lives;
    public float invulnerableTimer;
    /// im Bezug auf Rainbow Mode unverwundbarkeit
    public int deathZoneCounter;
    
    public static enum State{
        NORMAL,
        RAINBOW,
        HORNATTACK,
        SPUCKCHARGE
    }
    
    public State state = State.NORMAL;
    public float stateTimer;
    
    public float hornAttackCooldown;

    @Override
    public void reset() {
        doRespawn = false;
        maxHitpoints=0;
        hitpoints=0;
        lives=0;
        state=State.NORMAL;
        stateTimer=0;
        hornAttackCooldown=0;
        invulnerableTimer=0;
    }
   
}
