#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

#define CIRCLE_ANIMATION_GROW_FACTOR 0.4
#define CIRCLE_MAX_AMOUNT_FACTOR 1.0
// radius in px
#define CIRCLE_RADIUS_RANGE_FACTOR_START 1.0 
#define CIRCLE_RADIUS_RANGE_FACTOR_END 2.0
// threshold in px
#define ANTI_ALIASING_THRESHOLD 2.0
// random chosen number
#define SEED_STEP 78.1563
// duration in seconds
#define FLASH_DURATION 0.1
#define FADE_IN_DURATION 0.2
#define FADE_OUT_DURATION 0.4


varying LOWP vec4    v_color;
varying LOWP vec2    v_texCoords;

uniform LOWP vec2    u_frameDimension;
uniform LOWP float   u_time;
uniform LOWP float   u_startDuration;
uniform LOWP float   u_durationLeft;
uniform LOWP vec2    u_paparazziSeed;
// float [0.0, 0.1]
uniform LOWP float   u_paparazziIntensity;
// rgb: color to use for overlay. a: max alpha for result overlay
uniform LOWP vec4    u_paparazziColor;
// xy: circle center screen coordinates. z: circle radius
uniform LOWP vec3    u_paparazziOverlaySafeCircle;

uniform sampler2D u_texture;

float   getCircleAlpha(vec2 center, float radius);
vec2    getCircleCenter(vec2 seed);
float   getCircleRadius(vec2 seed);
float   getCircleAmount();
vec2    getRadiusRange();
float   getAnimProgress();
float   fade(float alphaIn);

// helper functions
float   rand(vec2 seed);
vec2    rand2(vec2 seed);
float   lerp(float a, float b, float t);
vec2    normalizedToScreen(vec2 normalized);

void main()
{
    // prevent LibGDX from throwing "uniform not used" exception
    float time = u_time;
    texture2D(u_texture, v_texCoords);
    
    float fragAlpha;
    for (int i = 0; i < getCircleAmount(); ++i)
    {
        fragAlpha += (u_paparazziColor.a - 0.2) * getCircleAlpha(getCircleCenter(u_paparazziSeed + i * SEED_STEP), getCircleRadius(u_paparazziSeed + i * SEED_STEP));
    }
    
    float finalAlpha = fade(clamp(fragAlpha, 0.0, u_paparazziColor.a));
    
    gl_FragColor = vec4(u_paparazziColor.r, u_paparazziColor.g, u_paparazziColor.b, finalAlpha);
}

vec2 getCircleCenter(vec2 seed)
{
    return normalizedToScreen(rand2(seed));
}

float getCircleRadius(vec2 seed)
{
    vec2 radiusRange = getRadiusRange();
    float radiusInBounds = rand(seed + 87.16854) * (radiusRange.y - radiusRange.x) + radiusRange.x;
    return radiusInBounds *= (1.0 + getAnimProgress() * CIRCLE_ANIMATION_GROW_FACTOR);
}

// returns distance between circle center and current gl_FragCoord
float getDistance(vec2 circleCenter)
{
    return length(gl_FragCoord.xy - circleCenter);
}

float getCircleAlpha(vec2 center, float radius)
{   
    
    float distance = getDistance(center);
    if (distance < (radius - ANTI_ALIASING_THRESHOLD))
    {
        return 1.0;
    }
    
    if (distance < radius)
    {
        return ((radius - distance) / ANTI_ALIASING_THRESHOLD);
    }

    return 0.0;
}

float getCircleAmount()
{
    return 
    30 * CIRCLE_MAX_AMOUNT_FACTOR * 
	    (
            // u_paparazziIntensity taken into account 20 percent
	        (1 - clamp(u_paparazziIntensity, 0.0, 1.0)) * 0.2 
	        + 0.8
	    );
} 

// returns vec2(minRadius, maxRadius)
vec2 getRadiusRange()
{
    float  baseFactor = max(u_frameDimension.x, u_frameDimension.y) * 0.0625; // 1/16
    baseFactor *=
        (
            // u_paparazziIntensity taken into account 40 percent
            (clamp(u_paparazziIntensity, 0.0, 1.0)) * 0.4 
            + 0.6
        );
    return vec2(baseFactor * CIRCLE_RADIUS_RANGE_FACTOR_START, baseFactor * CIRCLE_RADIUS_RANGE_FACTOR_END);
}

float fade(float alphaIn) {

    float distanceToSafeRegionCenter = (getDistance(u_paparazziOverlaySafeCircle.xy) - u_paparazziOverlaySafeCircle.z);
    // anti aliasing of safe center region border
    if (distanceToSafeRegionCenter <= ANTI_ALIASING_THRESHOLD)
    {
        alphaIn = alphaIn * ( distanceToSafeRegionCenter / ANTI_ALIASING_THRESHOLD );
        // continue with fading afterwards
    }

    // Flash in
    if (u_startDuration - u_durationLeft <= FLASH_DURATION)
    {
        return 1.0;
    }

    // Fade flash to normal
    if(u_startDuration - u_durationLeft <= FADE_IN_DURATION)
    {
        return alphaIn + (1.0 - alphaIn) * (1.0 - (((u_startDuration - u_durationLeft) - FLASH_DURATION) / FADE_IN_DURATION));
    }
    
    // Fade in
    //if(u_startDuration - u_durationLeft <= FADE_IN_DURATION)
    //{
    //    return alphaIn * (u_startDuration - u_durationLeft) * 4;
    //}
    
    // Fade out
    if(u_durationLeft <= FADE_OUT_DURATION)
    {
        return alphaIn * u_durationLeft * 2;
    }
    
    // exclude safe circle region from overlay
    if (distanceToSafeRegionCenter <= 0.0)
    {
        // no overlay in safe circle region
        return 0.0; 
    }
    
    // Default
    return alphaIn;
}

// lerps from a [t = 0] to b [t = 1]
float lerp(float a, float b, float t)
{
    return a * (1 - t) + b * t;
}

float getAnimProgress()
{
    return (u_startDuration - u_durationLeft) / u_startDuration;
}

// returns [0.0, 1.0]
float rand(vec2 seed)
{
    return abs(fract(sin(dot(seed.xy, vec2(12.9898,78.233))) * 43758.5453));
}

// returns vec2([0.0, 1.0], [0.0, 1.0])
vec2 rand2(vec2 seed)
{
    return vec2(rand(seed), rand(seed + 59.742));
}

vec2 normalizedToScreen(vec2 normalized)
{
    return normalized * u_frameDimension;
}