package de.hochschuletrier.gdw.ws1516.game.components;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class UnicornAnimationComponent extends AnimationComponent {
    public HashMap<String, HashMap<String, AnimationExtended>> unicornColoredAnimations;
    
    public void switchUnicornColor(UnicornColor color)
    {
        animationMap = unicornColoredAnimations.get(color.toString());
    }
    
    public enum UnicornColor
    {
        pink, blue, rainbow
    }
}