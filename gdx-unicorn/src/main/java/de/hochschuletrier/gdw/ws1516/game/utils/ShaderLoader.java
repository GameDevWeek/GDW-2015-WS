package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.hochschuletrier.gdw.ws1516.Main;

public class ShaderLoader {
    
    private static ShaderProgram fancyRainbowShader;
    private static ShaderProgram simpleRainbowShader;
    private static ShaderProgram oldRainbowShader;
    private static ShaderProgram paparazziShader;
    
    public static ShaderProgram getFancyRainbowShader()
    {
        if(fancyRainbowShader == null)
        {
            fancyRainbowShader = Main.getInstance().getAssetManager().getShaderProgram("fancyRainbow");
            System.out.println(fancyRainbowShader.getLog());
        }
        
        return fancyRainbowShader;
    }
    
    public static ShaderProgram getSimpleRainbowShader()
    {
        if(simpleRainbowShader == null)
        {
            simpleRainbowShader = Main.getInstance().getAssetManager().getShaderProgram("simpleRainbow");
            System.out.println(simpleRainbowShader.getLog());
        }
        
        return simpleRainbowShader;
    }
    
    public static ShaderProgram getOldRainbowShader()
    {
        if(oldRainbowShader == null)
        {
            oldRainbowShader = Main.getInstance().getAssetManager().getShaderProgram("oldRainbow");
            System.out.println(oldRainbowShader.getLog());
        }
        
        return oldRainbowShader;
    }
    
    public static ShaderProgram getPaparazziShader()
    {
        if(paparazziShader == null)
        {
            paparazziShader = Main.getInstance().getAssetManager().getShaderProgram("paparazzi");
            System.out.println(paparazziShader.getLog());
        }
        
        return paparazziShader;
    }
}