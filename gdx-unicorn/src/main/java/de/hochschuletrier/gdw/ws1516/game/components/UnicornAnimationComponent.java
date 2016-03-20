package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class UnicornAnimationComponent extends AnimationComponent {
    public HashMap<String, HashMap<String, AnimationExtended>> unicornColoredAnimations;

    public boolean isInBlueMode;
    public boolean isInRainbowMode;
    public boolean firedIdleEvent;
    
    public boolean isInDyingAnimation;
    
    public void switchUnicornColor(UnicornColor color)
    {
        if(color == UnicornColor.pink)
        {
            isInBlueMode = false;
            isInRainbowMode = false;
        }
        if(color == UnicornColor.blue)
        {
            isInBlueMode = true;
        }
        if(color == UnicornColor.rainbow)
        {
            isInRainbowMode = true;
        }
        animationMap = unicornColoredAnimations.get(color.toString());
    }
    
    public enum UnicornColor
    {
        pink, blue, rainbow
    }
}