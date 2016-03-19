package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.CollectableComponent.CollectableType;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent.EnemyType;
import de.hochschuletrier.gdw.ws1516.sandbox.gamelogic.GameLogicTest;

public class StartPointComponent extends Component implements Poolable {

    private static final Logger logger = LoggerFactory.getLogger(StartPointComponent.class);
    public static class SavedEntities {
        /// Das war dumm
        public static enum SaveableEntity  {
            BLUE_GUM("bubblegum_blue"),
            RAINBOW_GUM("bubblegum_rainbow"),
            HUNTER("hunter"),
            PAPARAZII("tourist");
            private String name;
            SaveableEntity(String name){
                this.name = name;
            }
            public String entityName(){
                return name;
            }
        }
        public ArrayList<Vector2> path;
        public Vector2 position;
        public Entity saved;
        public SaveableEntity entityType;
        public SavedEntities(Entity e) {
            PathComponent pathComp = ComponentMappers.path.get(e);
            PositionComponent pos = ComponentMappers.position.get(e);
            EnemyTypeComponent enemy = ComponentMappers.enemyType.get(e);
            CollectableComponent collect = ComponentMappers.collectable.get(e);
            
            entityType = SaveableEntity.PAPARAZII;
            if (enemy != null )
            {
                if ( enemy.type == EnemyType.HUNTER ) {
                    entityType = SaveableEntity.HUNTER;
                } else if (enemy.type == EnemyType.PAPARAZZI) {
                    entityType = SaveableEntity.PAPARAZII;
                }
            } else if ( collect != null ) {
                if ( collect.type == CollectableType.BLUE_GUM ) {
                    entityType = SaveableEntity.BLUE_GUM;
                } else if ( collect.type == CollectableType.RAINBOW_GUM ) {
                    entityType = SaveableEntity.RAINBOW_GUM;
                }
            }
            if ( pathComp != null ) {
                path = new ArrayList<Vector2>( pathComp.points.size() );
                for(Vector2 v:pathComp.points) path.add(new Vector2(v.x,v.y));
            }
            if ( pos != null ) {
                position = new Vector2(pos.x,pos.y);
            }else
            {
                logger.debug("keine Poisition");
            }
            saved = e;
        }
    }
    
    public ArrayList<SavedEntities> savedEntities = new ArrayList<SavedEntities>();
    public float x;
    public float y;
    public int blueGums;
    @Override
    public void reset() {
        x = y = blueGums = 0;
        savedEntities = new ArrayList<SavedEntities>();
    }

}
