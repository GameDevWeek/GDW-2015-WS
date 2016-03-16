package de.hochschuletrier.gdw.ws1516.game;

import com.badlogic.ashley.core.Family;

public class GameConstants {
    public static final float TILESIZE_X=64;
    public static final float TILESIZE_Y=64;
    // Priorities for entity systems
    public static final int PRIORITY_PHYSIX = 0;
    public static final int PRIORITY_ENTITIES = 10;
    public static final int PRIORITY_INPUT=11;
    public static final int PRIORITY_MOVEMENT=12;
    public static final int PRIORITY_CAMERA = 15;
    public static final int PRIORITY_MAP_RENDERING = 19;
    public static final int PRIORITY_RENDERING = 20;
    public static final int PRIORITY_DEBUG_WORLD = 30;
    public static final int PRIORITY_HUD = 40;
    public static final int PRIORITY_NAME = 50;
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
    public static final float PLAYER_SPEED = 100.0f;
    public static final float PLAYER_JUMP_IMPULSE = -500.0f;
    
    //Effect Times
    public static final float FLYING_TIME= 10.0f;
    public static final float RAINBOW_MODE_TIME = 10.0f;

    // Bullet system
    public static final float BULLET_SPEED = (50.0f / 43.0f) * 15.0f;
    
    public static float MUSIC_FADE_TIME = 2;
    
    // Scoreborad-Constant
    public static final float SCORE_TIME_POINTS = - 100 / 60;
    public static final int SCORE_CHOCOCOINS_POINTS = 1;
    public static final int SCORE_BONBONS_POINTS = 3;
    public static final int SCORE_DEATHS = 0; // negative Points ??
    public static final int SCORE_KILLED_ENEMIES = 0;
    public static final int SCORE_KILLED_OBSTACLES = 0;
    public static final int SCORE_HITS = 0;

    //Vision System
    public static final int GLOBAL_VISION=5;
    public static final int UNICORN_SIZE=(int) (TILESIZE_X*2);
    
    //EnemyBehaviour
    public static final int ENEMY_FRAME_JUMP_BUFFER = 60;
    //shaderParameter

    public static final float RAINBOW_FREQUENCY = 2.0f;
    public static final float RAINBOW_MODE_ALPHA = 0.4f;

}
