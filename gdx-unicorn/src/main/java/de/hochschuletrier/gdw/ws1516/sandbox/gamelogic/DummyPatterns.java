package de.hochschuletrier.gdw.ws1516.sandbox.gamelogic;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.ActionData;

public class DummyPatterns {
    
    
    public static class HunterData extends ActionData{

        boolean hasJumped;
        
    }
    public static ArrayList<Action> getHunterAttackPattern()
    {
        /// Datenpacket erstellen, dass alle ActionMethoden benutzten
        HunterData d = new HunterData();
        /// PatternListe erstellen
        ArrayList<Action> p = new  ArrayList<Action>();
        
        p.add(   new Action<HunterData>(d){
            protected void initStep()
            {
                d.hasJumped = false;
            };
            @Override
            protected int doStep(Entity entity,Entity unicorn) {
                /// Try to Shoot
                if ( !d.hasJumped )
                {
                    EnemyActionEvent.emit( entity,Action.Type.JUMP,-1000.0f);
                    d.hasJumped = true;
                }
                return (d.seconds>2)?1:0;
            }
            
        } );
        p.add( new Action<HunterData>(d){
            @Override
            protected int doStep(Entity entity,Entity unicorn) {
                if ( isLeftOf(unicorn, entity) )
                {                    
                    if ( velocityOf(entity) > -100.0f )
                    EnemyActionEvent.emit( entity,Action.Type.MOVE, -50.0f);
                } else
                {
                    if ( velocityOf(entity) < 100.0f )
                    EnemyActionEvent.emit( entity,Action.Type.MOVE, 50.0f);                    
                }
                return (d.seconds>2)?0:1;
            }

            
        } );
        return p;
    }

    private static  float velocityOf(Entity entity) {
        return ComponentMappers.physixBody.get(entity).getLinearVelocity().len();
    }
    private static boolean isLeftOf(Entity a,Entity b)
    {
        PhysixBodyComponent bA = ComponentMappers.physixBody.get(a);
        PhysixBodyComponent bB = ComponentMappers.physixBody.get(b);
        return bA.getPosition().x < bB.getPosition().x;
    }
    private static boolean isRightOf(Entity a,Entity b)
    {
        PhysixBodyComponent bA = ComponentMappers.physixBody.get(a);
        PhysixBodyComponent bB = ComponentMappers.physixBody.get(b);
        return bA.getPosition().x > bB.getPosition().x;
    }
}
