package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderLoader {
    
    private static ShaderProgram rainbowShader;
    private static ShaderProgram paparazziShader;
    
    public static ShaderProgram getRainbowShader()
    {
        if(rainbowShader == null)
        {
            String vertexShaderCode = Gdx.files.internal("data/shaders/rainbowVertexShader.glsl").readString();
            String fragmentShaderCode = Gdx.files.internal("data/shaders/rainbowFragmentShader.glsl").readString();
            rainbowShader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
            System.out.println(rainbowShader.getLog());
        }
        
        return rainbowShader;
    }
    
    public static ShaderProgram getPaparazziShader()
    {
        if(paparazziShader == null)
        {
            String vertexShaderCode = Gdx.files.internal("data/shaders/paparazziVertexShader.glsl").readString();
            String fragmentShaderCode = Gdx.files.internal("data/shaders/paparazziFragmentShader.glsl").readString();
            paparazziShader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
            System.out.println(paparazziShader.getLog());
        }
        
        return paparazziShader;
    }
}