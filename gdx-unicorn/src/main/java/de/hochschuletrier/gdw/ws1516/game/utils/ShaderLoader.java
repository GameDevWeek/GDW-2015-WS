package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.hochschuletrier.gdw.ws1516.Main;

public class ShaderLoader {

    private static ShaderProgram alphaTextureShader;
    private static ShaderProgram fancyRainbowShader;
    private static ShaderProgram simpleRainbowShader;
    private static ShaderProgram oldRainbowShader;
    private static ShaderProgram paparazziShader;
    private static ShaderProgram caveShader;
    
    static
    {
        ShaderProgram.pedantic = false;
    }
    
    public static ShaderProgram getAlphaTextureShader()
    {
        if(alphaTextureShader == null)
        {
            alphaTextureShader = Main.getInstance().getAssetManager().getShaderProgram("alphaTexture");
            if (!Main.IS_RELEASE) {
                System.out.println(alphaTextureShader.getLog());
            }
        }
        return alphaTextureShader;
    }
    
    public static ShaderProgram getFancyRainbowShader()
    {
        if(fancyRainbowShader == null)
        {
            fancyRainbowShader = Main.getInstance().getAssetManager().getShaderProgram("fancyRainbow");
            if (!Main.IS_RELEASE) {
                System.out.println(fancyRainbowShader.getLog());
            }
        }
        
        return fancyRainbowShader;
    }
    
    public static ShaderProgram getSimpleRainbowShader()
    {
        if(simpleRainbowShader == null)
        {
            simpleRainbowShader = Main.getInstance().getAssetManager().getShaderProgram("simpleRainbow");
            if (!Main.IS_RELEASE) {
                System.out.println(simpleRainbowShader.getLog());
            }
        }
        
        return simpleRainbowShader;
    }
    
    public static ShaderProgram getOldRainbowShader()
    {
        if(oldRainbowShader == null)
        {
            oldRainbowShader = Main.getInstance().getAssetManager().getShaderProgram("oldRainbow");
            if (!Main.IS_RELEASE) {
                System.out.println(oldRainbowShader.getLog());
            }
        }
        
        return oldRainbowShader;
    }
    
    public static ShaderProgram getPaparazziShader()
    {
        if(paparazziShader == null)
        {
            paparazziShader = Main.getInstance().getAssetManager().getShaderProgram("paparazzi");
            if (!Main.IS_RELEASE) {
                System.out.println(paparazziShader.getLog());
            }
        }
        
        return paparazziShader;
    }
    
    public static ShaderProgram getCaveShader()
    {
        if(caveShader == null)
        {
            caveShader = Main.getInstance().getAssetManager().getShaderProgram("cave");
            if (!Main.IS_RELEASE) {
                System.out.println(caveShader.getLog());
            }
        }
        
        return caveShader;
    }
}