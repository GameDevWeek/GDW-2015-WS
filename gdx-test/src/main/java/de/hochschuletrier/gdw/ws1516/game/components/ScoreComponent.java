package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ScoreComponent extends Component implements Pool.Poolable{

    public int chocoCoins;
    public int bubblegums;
    public int killedEnemies;
    public int killedObstacles;
    public int deaths;
    public int hits;
    
    @Override
    public void reset() {
        chocoCoins=0;
        bubblegums=0;
        killedEnemies=0;
        killedObstacles=0;
        deaths=0;
        hits=0;
    }

}
