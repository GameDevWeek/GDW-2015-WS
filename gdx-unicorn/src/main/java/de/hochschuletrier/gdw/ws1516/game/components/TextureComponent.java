package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent extends Component implements Pool.Poolable
{
    public Texture texture;
    public boolean flipHorizontal;
    public boolean flipVertical;
    public float alpha;
    public float originX;
    public float originY;
    
    @Override
    public void reset() {
        texture = null;
        flipHorizontal = false;
        flipVertical = false;
        alpha = 1.0f;
        originX = 0.0f;
        originY = 0.0f;
    } 
}