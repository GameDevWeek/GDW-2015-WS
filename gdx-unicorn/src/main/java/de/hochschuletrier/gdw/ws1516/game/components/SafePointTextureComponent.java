package de.hochschuletrier.gdw.ws1516.game.components;

import com.badlogic.gdx.graphics.Texture;

public class SafePointTextureComponent extends TextureComponent {
    public Texture activatedTexture;
    public Texture deactivatedTexture;
    
    @Override
    public void reset() {
        super.reset();
        activatedTexture = null;
        deactivatedTexture = null;
    }
}