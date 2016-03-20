package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.events.DeathEvent;
import de.hochschuletrier.gdw.ws1516.events.EnemyActionEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRespawnEvent;
import de.hochschuletrier.gdw.ws1516.events.GameRestartEvent;
import de.hochschuletrier.gdw.ws1516.events.HornAttackEvent;
import de.hochschuletrier.gdw.ws1516.events.MovementStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.PlayerStateChangeEvent;
import de.hochschuletrier.gdw.ws1516.events.SoundEvent;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.UnicornAnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.EnemyHandlingSystem.Action.Type;
import de.hochschuletrier.gdw.ws1516.game.utils.ShaderLoader;

public class AnimationRenderSystem extends SubSystem 
    implements MovementStateChangeEvent.Listener, PlayerStateChangeEvent.Listener, EnemyActionEvent.Listener, HornAttackEvent.Listener,
                DeathEvent.Listener, GameRespawnEvent.Listener, GameRestartEvent.Listener
{
    
    public Engine engine;
    
    @SuppressWarnings("unchecked")
    public AnimationRenderSystem() {
        super(Family.all(PositionComponent.class).one(AnimationComponent.class, UnicornAnimationComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animation = ComponentMappers.animation.get(entity);
       
        if(animation == null)
        {
            animation = ComponentMappers.unicornAnimation.get(entity);
        }
        
        
        PositionComponent position = ComponentMappers.position.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        PhysixBodyComponent physics = ComponentMappers.physixBody.get(entity);
        
        animation.stateTime += deltaTime;
        if((movement != null && movement.state != animation.lastRenderedState))
        {
            animation.resetStateTime();
            animation.lastRenderedState = movement.state;
        }
        
        TextureRegion keyFrame = null;
      
        if(movement != null)
        {
            boolean newFlipState = (movement.lookDirection) == (MovementComponent.LookDirection.LEFT) ^ animation.flipHorizontal;
            if(animation.currentlyFlipped != newFlipState)
            {
                animation.currentlyFlipped = newFlipState;
            }
            
            
            String stateKey = movement.state.toString().toLowerCase();
            
            if(uniteruptableAnimationRunning(entity, animation, movement))
            {
                keyFrame = getKeyFrameFromAnimationExtended(animation);
            }      
            else if(movement.state == MovementComponent.State.ON_GROUND)
            {
                keyFrame = getGroundKeyframe(animation, movement, stateKey, entity);
            }
            else if(movement.state == State.SHOOTING)
            {
                keyFrame = getShootingKeyFrame(animation, movement, stateKey);
            }
	        else if(movement.state == State.DYING)
        	{
            	keyFrame = getDyingKeyFrame(animation, movement, stateKey);
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
            
        }
        
        if(keyFrame == null)
        {
            keyFrame = getDefaultKeyframe(animation, entity);
        }
       
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
            
            float scaleX = animation.currentlyFlipped ? -1: 1;
            float xOff = animation.currentlyFlipped ? animation.xOffsetFlipped : animation.xOffset;
            DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f - xOff, position.y - h * 0.5f + animation.yOffset, w * 0.5f - xOff, h * 0.5f + animation.yOffset, w, h, scaleX, 1, position.rotation);
            if(animation.alpha < 1f && animation.alpha >= 0f)
            {
                DrawUtil.setShader(null);
            }
        }
    }

    private boolean uniteruptableAnimationRunning(Entity entity, AnimationComponent animation, MovementComponent movement) 
    {
        if(animation.uninteruptableAnimation == null)
        {
            return false;
        }
        
        if(animation.getClass() == UnicornAnimationComponent.class)
        {
            UnicornAnimationComponent unicornAnmation = (UnicornAnimationComponent)animation;
            if(unicornAnmation.isInDyingAnimation)
            {
                PlayerComponent player = ComponentMappers.player.get(entity);
                if(player != null)
                {
                    if(!player.doRespawn)
                    {
                        unicornAnmation.isInDyingAnimation = false;
                        unicornAnmation.uninteruptableAnimation = null;
                        unicornAnmation.resetStateTime();
                    }
                }
            }
        }
        
        if(animation.uninteruptableAnimation == null)
        {
            return false;
        }
        else if(animation.uninteruptableAnimation != null && animation.uninteruptableAnimation.getPlayMode() == PlayMode.NORMAL)
        {
            float animDuration = animation.uninteruptableAnimation.getDuration();
            float animTime = animation.stateTime;
            
            if(animTime > animDuration)
            {
                movement.state = State.ON_GROUND;
                return true;
            }
            
            animation.uninteruptableAnimation = null;
            return false;
        }
        else
        {
            return true;
        }
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

    private TextureRegion getGroundKeyframe(AnimationComponent animation, MovementComponent movement, String stateKey, Entity entity) {
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

            if(animationExtended != null && animation.getClass() == UnicornAnimationComponent.class)
            {
                UnicornAnimationComponent unicornAnimation = (UnicornAnimationComponent)animation;
                // Set back animation time if already in next loop
                if(unicornAnimation.stateTime > animationExtended.getDuration())
                {
                    unicornAnimation.resetStateTime();
                    unicornAnimation.firedIdleEvent = false;
                }
                if(animationExtended.getKeyFrameIndex(animation.stateTime) >= 1 && !unicornAnimation.firedIdleEvent)
                {
                    unicornAnimation.firedIdleEvent = true;
                    //UnicornIdleAnimationEvent.emit();
                    SoundEvent.emit("poop");
                }
            }
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
        return animationExtended.getKeyFrame(normalized *  animationExtended.getDuration());
    }

    private TextureRegion getLandingKeyframe(Entity entity, AnimationComponent animation, MovementComponent movement, String stateKey) {
        AnimationExtended animationExtended = animation.animationMap.get(MovementComponent.State.LANDING.toString().toLowerCase());
        if(animationExtended == null)
        {
            return null;
        }
        else if(animation.stateTime > animationExtended.getDuration())
        {
            movement.state = MovementComponent.State.ON_GROUND;
            MovementStateChangeEvent.emit(entity, MovementComponent.State.LANDING, MovementComponent.State.ON_GROUND);
            return getGroundKeyframe(animation, movement, stateKey, entity);
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
    
    private TextureRegion getDefaultKeyframe(AnimationComponent animation, Entity entity) {
        AnimationExtended animationExtended = animation.animationMap.get("on_ground_walking");
        if(animation.name.equals("Hunter"))
        {
            animationExtended = animation.animationMap.get("on_ground_idle");
        }
        if(animationExtended == null)
        {
            animationExtended = animation.animationMap.get("default_animation");
            if(animationExtended == null)
            {
                return null;
            }
        }
        
        //check if animation has to be deleted
        if(animationExtended != null)
        {
            float duration = animationExtended.getDuration();
            if(animation.killWhenFinished && animation.stateTime > duration)
            {
               DeathEvent.emit(entity);
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
    
    private TextureRegion getDyingKeyFrame(AnimationComponent animation, MovementComponent movement, String stateKey) 
    {
        AnimationExtended animationExtended = animation.animationMap.get(stateKey);
        
        if(animationExtended == null)
        {
            return null;
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

    @Override
    public void onEnemyActionEvent(Entity enemy, Type action, float strength) {
        AnimationComponent animationComponent = ComponentMappers.animation.get(enemy);     
      
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

    @Override
    public void onHornAttackStart(Entity player) {
        UnicornAnimationComponent animation = ComponentMappers.unicornAnimation.get(player);
        if(animation != null)
        {
            animation.resetStateTime();
            animation.uninteruptableAnimation = animation.animationMap.get("dash");
        }
    }

    @Override
    public void onHornAttackStop(Entity player) {
        UnicornAnimationComponent animation = ComponentMappers.unicornAnimation.get(player);
        if(animation != null)
        {
            animation.resetStateTime();
            animation.uninteruptableAnimation = null;
        }
    }

    @Override
    public void onGameRestartEvent() {
//        if(engine != null)
//        {
//            ImmutableArray<Entity> arr = engine.getEntitiesFor(Family.all(UnicornAnimationComponent.class).get());
//            for(Entity entity : arr)
//            {
//                UnicornAnimationComponent animation = ComponentMappers.unicornAnimation.get(entity);
//                animation.resetStateTime();
//                animation.uninteruptableAnimation = null;
//            }
//        }
    }

    @Override
    public void onGameRepawnEvent() {
//        if(engine != null)
//        {
//            ImmutableArray<Entity> arr = engine.getEntitiesFor(Family.all(UnicornAnimationComponent.class).get());
//            for(Entity entity : arr)
//            {
//                UnicornAnimationComponent animation = ComponentMappers.unicornAnimation.get(entity);
//                animation.resetStateTime();
//                animation.uninteruptableAnimation = null;
//            }
//        }
    }

    @Override
    public void onDeathEvent(Entity entity) {
        if(ComponentMappers.player.has(entity))
        {
            UnicornAnimationComponent animation = ComponentMappers.unicornAnimation.get(entity);
            if(animation != null)
            {
                animation.resetStateTime();
                animation.uninteruptableAnimation = animation.animationMap.get("dying");
                animation.isInDyingAnimation = true;
            }
        }
    }
}