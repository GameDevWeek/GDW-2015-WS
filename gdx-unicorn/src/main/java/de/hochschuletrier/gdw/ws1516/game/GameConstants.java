package de.hochschuletrier.gdw.ws1516.game;

import de.hochschuletrier.gdw.ws1516.game.utils.PhysixUtil;

public class GameConstants {
    public static final float TILESIZE_X=64;
    public static final float TILESIZE_Y=64;
    // Priorities for entity systems
    public static final int PRIORITY_PHYSIX = 0;
    public static final int PRIORITY_ENTITIES = 10;
    public static final int PRIORITY_INPUT=11;
    public static final int PRIORITY_MOVEMENT=12;
    public static final int PRIORITY_CAMERA = 15;
    public static final int PRIORITY_SPLATTER = 16;
    public static final int PRIORITY_MAP_RENDERING = 17;
    public static final int PRIORITY_RENDERING = 20;
    public static final int PRIORITY_EFFECTS_RENDERING = 25;
    public static final int PRIORITY_DEBUG_WORLD = 30;
    public static final int PRIORITY_HUD = 40;
    public static final int PRIORITY_NAME = 50;
    public static final int PRIORITY_REMOVE_ENTITIES = 1000;
    public static final int PRIORITY_DEATH_ANIMATION = 18;
    public static final int PRIORITY_ANIMATION_EVENTS = 999;
    public static final int PRIORITY_CAVE_LIGHTS_RENDERING = 900;
    

    // PooledEngine parameters
    public static final int ENTITY_POOL_INITIAL_SIZE = 32;
    public static final int ENTITY_POOL_MAX_SIZE = 256;
    public static final int COMPONENT_POOL_INITIAL_SIZE = 32;
    public static final int COMPONENT_POOL_MAX_SIZE = 256;

    // Physix parameters
    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int BOX2D_SCALE = 40;
    public static final float PLAYER_SPEED = 350.0f;
    public static final float PLAYER_JUMP_IMPULSE = -1250.0f;
    public static final float THROWBACK_FORCE = 800.0f;
    public static final float HORNATTACK_IMPULSE=1500.0f;
    public static final float PLATFORM_SPEED = 150.0f;
    
    //Effect Times
    public static final float FLYING_TIME= 10.0f;
    public static final float RAINBOW_MODE_TIME = 10.0f;
    public static final float SPUCK_MODE_TIME = 2.0f;
    public static final float SPUCK_MODE_COOLDOWN = 2.0f;
    public static final float THROWBACK_MODE_COOLDOWN = 0.75f;
    public static final float HORN_MODE_TIME = 0.75f;
    public static final float HORN_MODE_COOLDOWN = 3.0f;
    public static final float INVULNERABLE_TIMER = 0.5f;
    public static final float RAINBOW_SPEED_MODIFIER = 2.0f;
    
    //Physic collision groups
    public static final short PHYSIX_COLLISION_SPIT = -1;
    public static final short PHYSIX_COLLISION_UNICORN = -1;
    public static final short PHYSIX_COLLISION_BLOCKINGGUM = -1;
    public static final short PHYSIX_COLLISION_ENEMY = -2;
    
    // Bullet system
    public static final float BULLET_SPEED = (50.0f / 43.0f) * 15.0f;
    
    // Bubble-gum spit 
    public static final float SPIT_FORCE_MAX = 35.0f;
    public static final float SPIT_FORCE_MIN = 20.0f;
    public static final float SPIT_SPAWN_ANGLE = PhysixUtil.DEG2RAD * -25.0f;
    public static final float SPIT_SPAWN_OFFSET_X = 55.0f;
    public static final float SPIT_SPAWN_OFFSET_Y = -12.0f;
    public static final float SPIT_GLUE_COOLDOWN = 5.0f;
    public static final float SPIT_CHARGE_TIME_TO_MAX = 1.0f;
    public static final float SPIT_COOLDOWN = 1.0f;
    public static final float SPIT_BLOCKING_ORIGINX = 32.0f;
    public static final float SPIT_BLOCKING_ORIGINY = 16.0f;
    public static final float SPIT_BLOCKING_ALPHA_START = 0.8f;
    
    public static float MUSIC_FADE_TIME = 2;
    
    // Scoreboard-Constant
    public static final int SCORE_BASEPOINTS = 0;
    public static final float SCORE_TIME_POINTS = -100.0f/ 60.0f;
    public static final int SCORE_CHOCOCOINS_POINTS = 6;
    public static final int SCORE_BONBONS_POINTS = 35;
    public static final int SCORE_KILLED_ENEMIES = 0;
    public static final int SCORE_KILLED_OBSTACLES = 0;
    public static final int SCORE_HITS = 0;
    public static final int SCORE_LIVES = 100;

    //Vision System
    public static final int GLOBAL_VISION=5;
    public static final int UNICORN_SIZE=(int) (TILESIZE_X*2);
    
    //EnemyBehaviour
    public static final int ENEMY_FRAME_JUMP_BUFFER = 60; /// no longer used??
    public static final int HUNTER_BULLET_OFFSET = (int)TILESIZE_X;

    // Shader parameters
    public static final float RAINBOW_FREQUENCY = 5.0f;
    public static final float RAINBOW_ALPHA = 0.65f;
    public static final float RAINBOW_AMPLITUDE = 0.2f;
    public static final float PAPARAZZI_DURATION = 2.0f;
    public static final float CAVE_LIGHT_GLOW_RADIUS = (float) (1.5 * Math.max(TILESIZE_X, TILESIZE_Y));
    
    // Particle parameters
    public static final float IDLE_PARTICLE_REDUCTION = 0.2f;
    
    // Animation parameters

    public static final float TIME_TO_WAIT_TO_SHOOT = 0.5f;
    public static final double THROWBACK_ANIMATION_PERIODS = 3;

}
