package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.BubblegumSpitComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.enemyStates.EnemyBaseState;
import de.hochschuletrier.gdw.ws1516.game.systems.enemyStates.FollowPathEnemyState;

/**
 * Factory for the 'bubblegumSpit' component
 * @author Eileen
 * @version 1.0
 */
public class BubblegumSpitComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "BubblegumSpitComponent";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        BubblegumSpitComponent component = engine.createComponent(BubblegumSpitComponent.class);

        //Set dummy callback's to prevent nullpointer exceptions
        component.onEnemyHit = (spit, enemy) -> {};
        component.onHit = (spit) -> {};
        
        entity.add(component);
    }

}
