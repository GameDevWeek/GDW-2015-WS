package de.hochschuletrier.gdw.ws1516.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class GameConstants {

    // Priorities for entity systems
    public static final int PRIORITY_ENDGAME = 0;
    public static final int PRIORITY_SHOOT = 1;
    public static final int PRIORITY_PHYSIX = 10;
    public static final int PRIORITY_CANVAS = 20;
    public static final int PRIORITY_ANIMATIONS = 30;
    public static final int PRIORITY_DEBUG_WORLD = 40;
    public static final int PRIORITY_REMOVE_ENTITIES = 1000;

    // PooledEngine parameters
    public static final int ENTITY_POOL_INITIAL_SIZE = 32;
    public static final int ENTITY_POOL_MAX_SIZE = 256;
    public static final int COMPONENT_POOL_INITIAL_SIZE = 32;
    public static final int COMPONENT_POOL_MAX_SIZE = 256;

    // Physix parameters
    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int BOX2D_SCALE = 40;
    
    public static final float MUSIC_FADE_TIME = 2;
    
    public static final float CANVAS_SCALE_MENU = 0.715f;
    public static final float CANVAS_SCALE_MENU_REST = 1 - CANVAS_SCALE_MENU;
    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1024;
    
    public static final int PAINT_RADIUS = 10;
    public static final float BORDER_SIZE = 60;
    public static final float BOUND_LEFT = BORDER_SIZE + PAINT_RADIUS;
    public static final float BOUND_TOP = BORDER_SIZE + PAINT_RADIUS;
    public static final float BOUND_RIGHT = WINDOW_WIDTH - BORDER_SIZE - PAINT_RADIUS;
    public static final float BOUND_BOTTOM = WINDOW_HEIGHT - BORDER_SIZE - PAINT_RADIUS;
    public static final float BOUND_WIDTH = BOUND_RIGHT - BOUND_LEFT;
    public static final float BOUND_HEIGHT = BOUND_BOTTOM - BOUND_TOP;
    
    public static final float COLLISION_DISTANCE = PAINT_RADIUS * 1.7f;
    public static final float SEGMENT_DISTANCE = COLLISION_DISTANCE * 1.9f;
    public static final int PATH_STEP_SIZE = 10;
    public static final Rectangle PICKUP_SPAWN_RECT = new Rectangle(
            BOUND_LEFT + SEGMENT_DISTANCE,
            BOUND_TOP + SEGMENT_DISTANCE,
            BOUND_WIDTH - 2* SEGMENT_DISTANCE,
            BOUND_HEIGHT - 2* SEGMENT_DISTANCE
    );
}
