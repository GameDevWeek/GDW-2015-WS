package de.hochschuletrier.gdw.ws1516.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PathComponent;
import de.hochschuletrier.gdw.ws1516.game.components.StartPointComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.enemyStates.EnemyBaseState;
import de.hochschuletrier.gdw.ws1516.game.systems.enemyStates.FollowPathEnemyState;

public class EnemyBehaviourComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Behaviour";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        EnemyBehaviourComponent component = engine.createComponent(EnemyBehaviourComponent.class);

        component.currentState =  new FollowPathEnemyState();
        
        entity.add(component);
    }

}
