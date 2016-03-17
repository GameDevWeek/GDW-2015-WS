package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1516.events.PickupEvent;
import de.hochschuletrier.gdw.ws1516.events.SplashEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ProjectileComponent;
import de.hochschuletrier.gdw.ws1516.game.utils.PlayerColor;

public class CollisionSystem extends IteratingSystem {

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
        if(self != other && vposA.dst(posB.pos) < GameConstants.COLLISION_DISTANCE) {
            if(ComponentMappers.pickup.has(other))
                pickupHit(self, other);
            else if(ComponentMappers.player.has(other))
                headHit(self, other);
        } else {
            PlayerComponent playerB = ComponentMappers.player.get(other);
            if(playerB != null) {
                int index = 0;
                for (Vector2 vposB2 : playerB.segments) {
                    if(vposA.dst(vposB2) < GameConstants.COLLISION_DISTANCE) {
                        segmentHit(self, other, index);
                        return;
                    }
                    index++;
                }
            }
        }
    }
    
    private void pickupHit(Entity attacker, Entity victim) {
        PlayerComponent player = ComponentMappers.player.get(attacker);
        if(player != null) {
            PickupEvent.emit(attacker, victim);
            PositionComponent pos = ComponentMappers.position.get(attacker);
            PositionComponent otherPos = ComponentMappers.position.get(victim);
            InputComponent input = ComponentMappers.input.get(attacker);
            player.segments.addFirst(pos.pos.cpy());
            delta.set(input.lastMoveDirection).nor().scl(GameConstants.SEGMENT_DISTANCE);
            pos.pos.add(delta);
            engine.removeEntity(victim);
        }
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
            if(attacker == victim)
                segment -= 3;
        } else if(ComponentMappers.projectile.has(attacker)) {
            engine.removeEntity(attacker);
        }
        
        AnimationComponent anim = ComponentMappers.animation.get(victim);
        PlayerComponent player = ComponentMappers.player.get(victim);
        if(segment > 0) {
            while(player.segments.size() > segment) {
                explodeAt(player.segments.removeLast(), player.color);
            }
        }
    }

    private void receiveHeadDamage(Entity entity) {
        PositionComponent pos = ComponentMappers.position.get(entity);
        AnimationComponent anim = ComponentMappers.animation.get(entity);
        PlayerComponent player = ComponentMappers.player.get(entity);
        final int size = player.segments.size();
        if(size <= 2) {
            explodeAt(pos.pos, player.color);
            for(int i=0; i<size; i++) {
                explodeAt(player.segments.removeFirst(), player.color);
            }
            //fixme: die
        } else {
            for(int i=0; i<3; i++) {
                explodeAt(player.segments.get(i), player.color);
            }
            pos.pos.set(player.removeFirstSegments(3));
        }
    }

    private void explodeAt(Vector2 first, PlayerColor color) {
        SplashEvent.emit(first,color);
    }
}
