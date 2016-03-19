package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent extends Component implements Pool.Poolable
{
    public Texture texture;
    public boolean flipHorizontal;
    public boolean flipVertical;
    
    public float originX;
    public float originY;
    
    public float alpha;
    
    @Override
    public void reset() {
        texture = null;
    } 
}