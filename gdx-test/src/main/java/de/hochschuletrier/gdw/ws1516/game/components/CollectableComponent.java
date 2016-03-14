package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CollectableComponent extends Component implements Poolable{

    public Entity unicorn;
    public Entity collected;
    public Consumer<CollectableComponent> onCollect;
    
    @Override
    public void reset() {
        onCollect = new Consumer<CollectableComponent>() {

            @Override
            public void accept(CollectableComponent t) {
                ScoreComponent score= t.unicorn.getComponent(ScoreComponent.class);
                if ( t.collected.getComponent(IsChocoCookie.class) != null )
                {
                score.chocoCoins ++;
                }               
            }
        };
    }

}
