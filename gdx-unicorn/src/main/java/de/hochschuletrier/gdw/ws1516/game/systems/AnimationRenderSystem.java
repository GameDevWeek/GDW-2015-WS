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

public class AnimationRenderSystem extends SubSystem{
    
    public AnimationRenderSystem() {
        super(Family.all(PositionComponent.class, AnimationComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
                
        AnimationComponent animation = ComponentMappers.animation.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        animation.stateTime += deltaTime;
        TextureRegion keyFrame = null; //animation.animation.getKeyFrame(animation.stateTime);

        if(animation.animationState != AnimationState.none)
        {
            keyFrame = animation.animationMap.get(animation.animationState.toString()).getKeyFrame(animation.stateTime);
        }
        else
        {
            keyFrame = animation.animationMap.get("animation").getKeyFrame(animation.stateTime);
        }
        
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

        
//        AnimationComponent animation = ComponentMappers.animation.get(entity);
//        PositionComponent position = ComponentMappers.position.get(entity);
//
//        animation.stateTime += deltaTime;
//        TextureRegion keyFrame = animation.animation.getKeyFrame(animation.stateTime);
//        int w = keyFrame.getRegionWidth();
//        int h = keyFrame.getRegionHeight();
//        DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation);
    }
}