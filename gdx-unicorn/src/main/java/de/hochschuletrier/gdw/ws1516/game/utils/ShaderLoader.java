package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.hochschuletrier.gdw.ws1516.Main;

public class ShaderLoader {
    
    private static ShaderProgram fancyRainbowShader;
    private static ShaderProgram simpleRainbowShader;
    private static ShaderProgram paparazziShader;
    
    public static ShaderProgram getFancyRainbowShader()
    {
        if(fancyRainbowShader == null)
        {
//            String vertexShaderCode = Gdx.files.internal("data/shaders/fancyRainbowVertexShader.glsl").readString();
//            String fragmentShaderCode = Gdx.files.internal("data/shaders/fancyRainbowFragmentShader.glsl").readString();
//            rainbowShader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
            fancyRainbowShader = Main.getInstance().getAssetManager().getShaderProgram("fancyRainbow");
            System.out.println(fancyRainbowShader.getLog());
        }
        
        return fancyRainbowShader;
    }
    
    public static ShaderProgram getSimpleRainbowShader()
    {
        if(simpleRainbowShader == null)
        {
//            String vertexShaderCode = Gdx.files.internal("data/shaders/simpleRainbowVertexShader.glsl").readString();
//            String fragmentShaderCode = Gdx.files.internal("data/shaders/simpleRainbowFragmentShader.glsl").readString();
//            simpleRainbowShader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
            simpleRainbowShader = Main.getInstance().getAssetManager().getShaderProgram("simpleRainbow");
            System.out.println(simpleRainbowShader.getLog());
        }
        
        return simpleRainbowShader;
    }
    
    public static ShaderProgram getPaparazziShader()
    {
        if(paparazziShader == null)
        {
//            String vertexShaderCode = Gdx.files.internal("data/shaders/paparazziVertexShader.glsl").readString();
//            String fragmentShaderCode = Gdx.files.internal("data/shaders/paparazziFragmentShader.glsl").readString();
//            paparazziShader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
            paparazziShader = Main.getInstance().getAssetManager().getShaderProgram("paparazzi");
            System.out.println(paparazziShader.getLog());
        }
        
        return paparazziShader;
    }
}