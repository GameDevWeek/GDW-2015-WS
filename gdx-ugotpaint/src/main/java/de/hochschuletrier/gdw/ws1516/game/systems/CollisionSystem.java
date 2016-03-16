package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ProjectileComponent;

public class CollisionSystem extends IteratingSystem {

    public static final float COLLISION_DISTANCE = 12;
    public static final float SEGMENT_DISTANCE = 18;
    private Engine engine;
    private final Vector2 delta = new Vector2();
    private ImmutableArray<Entity> otherEntitites;

    public CollisionSystem() {
        this(0);
    }

    public CollisionSystem(int priority) {
        super(Family.one(PlayerComponent.class, ProjectileComponent.class).get(), priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        otherEntitites = engine.getEntitiesFor(Family.all(PositionComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        for (Entity other : otherEntitites) {
            if(!entity.isScheduledForRemoval() && !other.isScheduledForRemoval())
                checkCollision(entity, other);
        }
    }

    private void checkCollision(Entity self, Entity other) {
        PositionComponent posA = ComponentMappers.position.get(self);
        PositionComponent posB = ComponentMappers.position.get(other);
        final Vector2 vposA = posA.pos;
        if(self != other && vposA.dst(posB.pos) < COLLISION_DISTANCE) {
            if(ComponentMappers.pickup.has(other))
                pickupHit(self, other);
            else if(ComponentMappers.player.has(other))
                headHit(self, other);
        } else {
            PlayerComponent playerB = ComponentMappers.player.get(other);
            if(playerB != null) {
                int index = 0;
                for (Vector2 vposB2 : playerB.segments) {
                    if((self != other || index > 1) && vposA.dst(vposB2) < COLLISION_DISTANCE) {
                        segmentHit(self, other, index);
                        return;
                    }
                    index++;
                }
            }
        }
    }
    
    private void pickupHit(Entity attacker, Entity victim) {
        // todo: add segment
        PositionComponent pos = ComponentMappers.position.get(attacker);
        PositionComponent otherPos = ComponentMappers.position.get(victim);
        PlayerComponent player = ComponentMappers.player.get(attacker);
        player.segments.addFirst(pos.pos.cpy());
        delta.set(otherPos.pos).sub(pos.pos).nor().scl(SEGMENT_DISTANCE);
        pos.pos.add(delta);
        engine.removeEntity(victim);
    }
    
    private void headHit(Entity attacker, Entity victim) {
        receiveHeadDamage(victim);
        if(ComponentMappers.player.has(attacker)) {
            receiveHeadDamage(attacker);
        }
    }
    
    private void segmentHit(Entity attacker, Entity victim, int segment) {
        if(ComponentMappers.player.has(attacker)) {
            receiveHeadDamage(attacker);
        }
        
        PositionComponent pos = ComponentMappers.position.get(victim);
        AnimationComponent anim = ComponentMappers.animation.get(victim);
        PlayerComponent player = ComponentMappers.player.get(victim);
        while(player.segments.size() > segment) {
            explodeAt(player.segments.removeLast(), anim.tint);
        }
    }

    private void receiveHeadDamage(Entity entity) {
        PositionComponent pos = ComponentMappers.position.get(entity);
        AnimationComponent anim = ComponentMappers.animation.get(entity);
        PlayerComponent player = ComponentMappers.player.get(entity);
        final int size = player.segments.size();
        if(size <= 2) {
            explodeAt(pos.pos, anim.tint);
            for(int i=0; i<size; i++) {
                explodeAt(player.segments.removeFirst(), anim.tint);
            }
            //fixme: die
        } else {
            Vector2 first = pos.pos;
            for(int i=0; i<3; i++) {
                explodeAt(first, anim.tint);
                first = player.segments.removeFirst();
            }
            pos.pos.set(first);
        }
    }

    private void explodeAt(Vector2 first, Color tint) {
    }
}
