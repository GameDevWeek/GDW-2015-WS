package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.events.MovementStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class AnimationRenderSystem extends SubSystem implements MovementStateChangeEvent.Listener, PlayerStateChangeEvent.Listener
{
    public AnimationRenderSystem() {
        super(Family.all(PositionComponent.class, AnimationComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animation = ComponentMappers.animation.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        PhysixBodyComponent physics = ComponentMappers.physixBody.get(entity);
        
        if(animation.name.equals("Paparazzi"))
        {
            System.out.println("Test");
        }
        
        animation.stateTime += deltaTime;
        if(movement != null && movement.state != animation.lastRenderedState)
        {
            animation.resetStateTime();
            animation.lastRenderedState = movement.state;
        }
        
        TextureRegion keyFrame = null;

        String stateKey = movement.state.toString().toLowerCase();
        if(movement.state == MovementComponent.State.ON_GROUND)
        {
            keyFrame = getGroundKeyframe(animation, movement, stateKey);
        }
        else if((movement.state == State.JUMPING || movement.state == State.FALLING) && physics != null)
        {
            keyFrame = getAirKeyframe(animation, movement, physics);
        }
        else if(movement.state == State.LANDING)
        {
            keyFrame = getLandingKeyframe(entity, animation, movement, stateKey);
        }
        else {
            keyFrame = getOtherKeyframe(animation, stateKey);
        }
        
        if(keyFrame == null)
        {
            keyFrame = getDefaultKeyframe(animation);
        }
        
        animation.currentlyFlipped = (movement.lookDirection) == (MovementComponent.LookDirection.LEFT) ^ animation.flipHorizontal;
        
        if(keyFrame != null)
        {
            int w = keyFrame.getRegionWidth();
            int h = keyFrame.getRegionHeight();
            
            if(animation.currentlyFlipped)
            {
                DrawUtil.batch.draw(keyFrame, position.x + w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, -w, h, 1, 1, position.rotation);
            }
            else
            {
                DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation);
            }
        }        
    }

    public void registerListeners()
    {
        MovementStateChangeEvent.register(this);
        PlayerStateChangeEvent.register(this);
    }
    
    public void unregisterListeners()
    {
        MovementStateChangeEvent.unregister(this);
        PlayerStateChangeEvent.unregister(this);
    }

    private TextureRegion getGroundKeyframe(AnimationComponent animation, MovementComponent movement, String stateKey) {
        String idleString = stateKey + "_idle";
        String walkingString = stateKey + "_walking";
        
        AnimationExtended animationExtended = null;
        if(Math.abs(movement.velocityX) <= 0.01)
        {
            animationExtended = animation.animationMap.get(idleString);
        }
        else
        {
            animationExtended = animation.animationMap.get(walkingString);
        }
        return animationExtended == null ? null : animationExtended.getKeyFrame(animation.stateTime);
    }

    private TextureRegion getAirKeyframe(AnimationComponent animation, MovementComponent movement, PhysixBodyComponent physics) {
        AnimationExtended animationExtended = animation.animationMap.get(MovementComponent.State.JUMPING.toString().toLowerCase());
        if(animationExtended == null)
        {
            return null;
        }
        float normalized = physics.getLinearVelocity().y / 500f;
        if(normalized < -1f)
            normalized = -1f;
        else if(normalized > 1f)
            normalized = 1f;
        normalized = (normalized + 1) * 0.5f;
        return animationExtended.getKeyFrame(normalized *  animationExtended.animationDuration);
    }

    private TextureRegion getLandingKeyframe(Entity entity, AnimationComponent animation, MovementComponent movement, String stateKey) {
        AnimationExtended animationExtended = animation.animationMap.get(MovementComponent.State.LANDING.toString().toLowerCase());
        if(animationExtended == null)
        {
            return null;
        }
        else if(animation.stateTime > animationExtended.animationDuration)
        {
            movement.state = MovementComponent.State.ON_GROUND;
            MovementStateChangeEvent.emit(entity, MovementComponent.State.LANDING, MovementComponent.State.ON_GROUND);
            return getGroundKeyframe(animation, movement, stateKey);
        }
        return animationExtended.getKeyFrame(animation.stateTime);
    }

    private TextureRegion getOtherKeyframe(AnimationComponent animation, String stateKey) {
        AnimationExtended animationExtended = animation.animationMap.get(stateKey);
        if(animationExtended == null)
        {
            return null;
        }
        return animationExtended.getKeyFrame(animation.stateTime);
    }
    
    private TextureRegion getDefaultKeyframe(AnimationComponent animation) {
        AnimationExtended animationExtended = animation.animationMap.get("on_ground_walking");
        if(animationExtended == null)
        {
            animationExtended = animation.animationMap.get("animation");
            if(animationExtended == null)
            {
                return null;
            }
        }
        return animationExtended.getKeyFrame(animation.stateTime);
    }

    @Override
    public void onPlayerStateChangeEvent(Entity entity, State oldState, State newState) {
        AnimationComponent animationComponent = ComponentMappers.animation.get(entity);
        if(animationComponent != null)
        {
            animationComponent.resetStateTime();
        }
    }

    @Override
    public void onMovementStateChangeEvent(Entity entity, State oldState, State newState) {
        AnimationComponent animationComponent = ComponentMappers.animation.get(entity);
        if(animationComponent != null)
        {
            animationComponent.resetStateTime();
        }
    }
}