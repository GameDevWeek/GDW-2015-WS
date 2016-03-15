package de.hochschuletrier.gdw.ws1516.game;

public class GameConstants {

    // Priorities for entity systems
    public static final int PRIORITY_PHYSIX = 0;
    public static final int PRIORITY_ENTITIES = 10;
    public static final int PRIORITY_ANIMATIONS = 20;
    public static final int PRIORITY_DEBUG_WORLD = 30;
    public static final int PRIORITY_HUD = 40;
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
    
    public static float MUSIC_FADE_TIME = 2;
    
    // Scoreborad-Constant
    public static float SCORE_TIME_POINTS = - 100 / 60;
    public static final int SCORE_CHOCOCOINS_POINTS = 1;
    public static final int SCORE_BONBONS_POINTS = 3;
    public static final int SCORE_DEATHS = 0; // negative Points ??
    public static final int SCORE_KILLED_ENEMIES = 0;
    public static final int SCORE_KILLED_OBSTACLES = 0;
    public static final int SCORE_HITS = 0;
    
}
