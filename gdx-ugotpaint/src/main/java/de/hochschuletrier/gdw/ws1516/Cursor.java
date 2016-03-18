package de.hochschuletrier.gdw.ws1516;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

public class Cursor {

    private static Pixmap cursor;

    public static void init() {
        cursor = new Pixmap(Gdx.files.internal("data/images/cursor.png"));
    }

    public static void enable() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setCursorImage(cursor, 35, 0);
    }

    public static void disable() {
        Gdx.input.setCursorImage(null, 0, 0);
        Gdx.input.setCursorCatched(true);
    }
}
