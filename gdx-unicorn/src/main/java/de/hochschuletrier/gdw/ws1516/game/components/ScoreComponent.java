package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ScoreComponent extends Component implements Pool.Poolable{

    /// time already spent in Game
    public float playedSeconds;
    
    public int chocoCoins; // 1
    public int bonbons; // 3
    public int bubblegums;
    public int killedEnemies;
    public int killedObstacles;
    public int hits;
    public int lives = 3;
    
    @Override
    public void reset() {
        playedSeconds = 0;
        bonbons=0;
        chocoCoins=0;
        bubblegums=0;
        killedEnemies=0;
        killedObstacles=0;
        lives=3;
        hits=0;
    }

}
