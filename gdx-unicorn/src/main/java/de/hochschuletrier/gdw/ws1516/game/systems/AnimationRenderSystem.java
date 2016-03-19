package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.events.MovementStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyBehaviourComponent;
import de.hochschuletrier.gdw.ws1516.game.components.EnemyTypeComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;

public class AnimationRenderSystem extends SubSystem 
    implements MovementStateChangeEvent.Listener, PlayerStateChangeEvent.Listener, EnemyActionEvent.Listener
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
        
        EnemyTypeComponent enemyType = ComponentMappers.enemyType.get(entity);
        EnemyBehaviourComponent enemyBehaviour = ComponentMappers.enemyBehaviour.get(entity);
        
        
        if(animation.name.equals("Paparazzi") || animation.name.equals("Hunter"))
        {
//            System.out.println(enemyType.type);
//            System.out.println(enemyBehaviour.canSeeUnicorn + " " + enemyBehaviour.currentState.toString());
        }
        
        animation.stateTime += deltaTime;
        if((movement != null && movement.state != animation.lastRenderedState))
        {
            animation.resetStateTime();
            animation.lastRenderedState = movement.state;
        }
        
        TextureRegion keyFrame = null;

        String stateKey = movement.state.toString().toLowerCase();
        
        if(animation.name.equals("Hunter") && uniteruptableAnimationRunning(animation, movement))
        {
//            System.out.println(animation.uninteruptableAnimationBool);
            System.out.println(movement.state.toString());
            keyFrame = getKeyFrameFromAnimationExtended(animation);
        }      
        else if(movement.state == MovementComponent.State.ON_GROUND)
        {
            keyFrame = getGroundKeyframe(animation, movement, stateKey);
        }
        else if(movement.state == State.SHOOTING)
        {
            keyFrame = getShootingKeyFrame(animation, movement, stateKey);
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
        
        drawKeyframe(animation, position, keyFrame);   
    }

    private void drawKeyframe(AnimationComponent animation, PositionComponent position, TextureRegion keyFrame) {
        if(keyFrame != null)
        {
            if(animation.alpha < 1f && animation.alpha >= 0f)
            {
                ShaderProgram alphaTextureShader = ShaderLoader.getAlphaTextureShader();
                DrawUtil.setShader(alphaTextureShader);
                alphaTextureShader.setUniformf("u_alpha", animation.alpha);
            }
            int w = keyFrame.getRegionWidth();
            int h = keyFrame.getRegionHeight();
            
            if(animation.currentlyFlipped)
            {
                DrawUtil.batch.draw(keyFrame, position.x + w * 0.5f + animation.xOffset, position.y - h * 0.5f + animation.yOffset, w * 0.5f + animation.xOffset, h * 0.5f + animation.yOffset, -w, h, 1, 1, position.rotation);
            }
            else
            {
                DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f - animation.xOffset, position.y - h * 0.5f + animation.yOffset, w * 0.5f - animation.xOffset, h * 0.5f + animation.yOffset, w, h, 1, 1, position.rotation);
            }
            if(animation.alpha < 1f && animation.alpha >= 0f)
            {
                DrawUtil.setShader(null);
            }
        }
    }

    private boolean uniteruptableAnimationRunning(AnimationComponent animation, MovementComponent movement) 
    {
        if(animation.uninteruptableAnimation == null)
        {
            return false;
        }
        
        float animDuration = animation.uninteruptableAnimation.animationDuration;
        float animTime = animation.stateTime;
        
        if(animTime > animDuration)
        {
            movement.state = State.ON_GROUND;
            return true;
        }
        
        animation.uninteruptableAnimation = null;
        return false;
    }

    public void registerListeners()
    {
        MovementStateChangeEvent.register(this);
        PlayerStateChangeEvent.register(this);
        EnemyActionEvent.register(this);
    }
    
    public void unregisterListeners()
    {
        MovementStateChangeEvent.unregister(this);
        PlayerStateChangeEvent.unregister(this);
        EnemyActionEvent.unregister(this);
    }

    private TextureRegion getGroundKeyframe(AnimationComponent animation, MovementComponent movement, String stateKey) {
        String idleString = stateKey + "_idle";
        String walkingString = stateKey + "_walking";
        
        AnimationExtended animationExtended = null;
        boolean isIdle = Math.abs(movement.velocityX) <= 0.01;
        
        if((isIdle != animation.lastRenderedIdle))
        {
            animation.resetStateTime();
            animation.lastRenderedIdle = isIdle;
        }
        
        if(isIdle)
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
        if(animation.name.equals("Hunter"))
        {
            animationExtended = animation.animationMap.get("on_ground_idle");
        }
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
    
    private TextureRegion getShootingKeyFrame(AnimationComponent animation, MovementComponent movement, String stateKey)
    {
        AnimationExtended animationExtended = animation.animationMap.get(stateKey);
        
        if(animationExtended == null)
        {
            return null;
        }
        
        animation.uninteruptableAnimation = animationExtended;
        
        return animationExtended.getKeyFrame(animation.stateTime);
    }
    
    private TextureRegion getKeyFrameFromAnimationExtended(AnimationComponent animation) 
    {
       return animation.uninteruptableAnimation.getKeyFrame(animation.stateTime);
    }

    @Override
    public void onPlayerStateChangeEvent(Entity entity, State oldState, State newState) {
//        System.out.println("Player State Change");
        AnimationComponent animationComponent = ComponentMappers.animation.get(entity);
        if(animationComponent != null)
        {
            animationComponent.resetStateTime();
        }
    }

    @Override
    public void onMovementStateChangeEvent(Entity entity, State oldState, State newState) {
//        System.out.println("Movement State Change");
        AnimationComponent animationComponent = ComponentMappers.animation.get(entity);
        if(animationComponent != null)
        {
            animationComponent.resetStateTime();
        }
    }

    @Override
    public void onEnemyActionEvent(Entity enemy, Type action, float strength) {
        AnimationComponent animationComponent = ComponentMappers.animation.get(enemy);
        
        EnemyTypeComponent enemyType = ComponentMappers.enemyType.get(enemy);
        EnemyBehaviourComponent enemyBehaviour = ComponentMappers.enemyBehaviour.get(enemy);       
        
        MovementComponent movement = ComponentMappers.movement.get(enemy);
        
        if((animationComponent.name.equals("Paparazzi") || animationComponent.name.equals("Hunter")) && (action == EnemyHandlingSystem.Action.Type.SHOOT))
        {
            movement.state = State.SHOOTING;
        }
        
        if(animationComponent != null)
        {
            animationComponent.resetStateTime();
        }
    }
}