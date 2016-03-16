package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderLoader {
    
    private static ShaderProgram rainbowShader;
    
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
}