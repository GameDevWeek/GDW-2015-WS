package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent.AnimationState;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.events.*;

public class AnimationRenderSystem extends SubSystem 
    implements MovementEvent.Listener, JumpEvent.Listener, StartFlyEvent.Listener, EndFlyEvent.Listener
{
    
    public AnimationRenderSystem() {
        super(Family.all(PositionComponent.class, AnimationComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
                
        AnimationComponent animation = ComponentMappers.animation.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        animation.stateTime += deltaTime;
        TextureRegion keyFrame = null;

        if(animation.animationState != AnimationState.none && animation.animationMap.get(animation.animationState) != null)
        {
            keyFrame = animation.animationMap.get(animation.animationState).getKeyFrame(animation.stateTime);
        }
        else if(animation.animationMap.get(AnimationState.none) != null)
        {
            keyFrame = animation.animationMap.get(AnimationState.none).getKeyFrame(animation.stateTime);
        }
        
        if(keyFrame != null)
        {
            int w = keyFrame.getRegionWidth();
            int h = keyFrame.getRegionHeight();
            
            if(animation.flipHorizontal)
            {
                DrawUtil.batch.draw(keyFrame, position.x + w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, -w, h, 1, 1, position.rotation);
            }
            else
            {
                DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation);
            }
        }
        
    }
    
    private boolean move = false;

    @Override
    public void onMovementEvent(Entity entity, float dirX) 
    {       
        AnimationComponent animation = ComponentMappers.animation.get(entity);
//        System.out.println("Entity " + animation.animationState + " " + animation.animationMap.get(AnimationState.none).getFrameCount());
        if(animation != null)
        {
//            if(Math.abs(dirX) >= 0.01f && AnimationState.walking.compareTo(animation.animationState) < 0)
//            {
//                animation.animationState = AnimationState.walking;
//                animation.flipHorizontal = dirX < 0.0f;
//            }
              if(Math.abs(dirX) <= 0.01f)
              {
                  animation.animationState = AnimationState.none;
              }
              else
              {
                  animation.animationState = AnimationState.walking;
                  animation.flipHorizontal = dirX < 0.0f;
              }
        }
    }

    @Override
    public void onJumpEvent(Entity entity)
    {
//        System.out.println(move);
        AnimationComponent animation = ComponentMappers.animation.get(entity);
//        System.out.println(AnimationState.jump.compareTo(animation.animationState) > 0);
//        if(animation != null && AnimationState.jump.compareTo(animation.animationState) < 0)
//        {
//            animation.animationState = AnimationState.jump;
//        }
        if(animation != null)
        {
            animation.animationState = AnimationState.jump;
        }
    }

    @Override
    public void onStartFlyEvent(Entity entity, float time) 
    {
        // TODO Auto-generated method stub
        System.out.println("fly" + time);
        
        AnimationComponent animation = ComponentMappers.animation.get(entity);
        
        if(animation != null)
        {
            animation.animationState = AnimationState.fly;
        }

    }
    
    @Override
    public void onEndFlyEvent(Entity entity) 
    {
        // TODO Auto-generated method stub
        System.out.println("end fly");
        
        AnimationComponent animation = ComponentMappers.animation.get(entity);

        if(animation != null)
        {
            animation.animationState = AnimationState.none;
        }
        
    }
}