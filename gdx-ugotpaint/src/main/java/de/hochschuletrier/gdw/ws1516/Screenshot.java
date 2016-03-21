package de.hochschuletrier.gdw.ws1516;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;

public class Screenshot {
    public static void take() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT, true);

        Pixmap pixmap = new Pixmap(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT, Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        PixmapIO.writePNG(getFile(), pixmap);
        pixmap.dispose();
    }
    
    private static FileHandle getFile() {
        int i=0;
        FileHandle file;
        do {
            file = Gdx.files.external(getPrefix(i++) + ".png");
        } while(file.exists());
        return file;
    }
    
    private static String getPrefix(int i) {
        if(i<10)
            return "HueGotPaint/shot_000" + i;
        if(i<100)
            return "HueGotPaint/shot_00" + i;
        if(i<1000)
            return "HueGotPaint/shot_0" + i;
        return "HueGotPaint/shot_" + i;
    }
}
