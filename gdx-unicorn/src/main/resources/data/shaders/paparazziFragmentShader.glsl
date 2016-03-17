#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

#define M_PI 3.1415926535897932384626433832795
#define M_2PI 6.283185307179586476925286766559
#define M_3PI 9.4247779607693797153879301498385

#define CIRCLE_ANIMATION_GROW_FACTOR 0.1
#define CIRCLE_MAX_AMOUNT_FACTOR 1.0
// radius in px
#define CIRCLE_RADIUS_RANGE_FACTOR_START 1.0 
#define CIRCLE_RADIUS_RANGE_FACTOR_END 2.0
// threshold in px
#define ANTI_ALIASING_THRESHOLD 2.0
// random chosen numbers
#define SEED_STEP_A 78.1563
#define SEED_STEP_B 87.16854
#define SEED_STEP_C 59.742
// duration in seconds
#define PRE_INTRO_DURATION 0.1
#define INTRO_DURATION 0.2
#define OUTRO_DURATION 0.4


varying LOWP vec4    v_color;
varying LOWP vec2    v_texCoords;

uniform LOWP vec2    u_frameDimension;
uniform LOWP float   u_time;
uniform LOWP float   u_effectDuration;
uniform LOWP float   u_remainingEffectDuration;
uniform LOWP vec2    u_paparazziSeed;
// float [0.0, 0.1]
uniform LOWP float   u_paparazziIntensity;
// rgb: color to use for overlay. a: max alpha for result overlay
uniform LOWP vec4    u_paparazziColor;
// xy: circle center screen coordinates. z: circle radius
uniform LOWP vec3    u_paparazziOverlaySafeCircle;

uniform sampler2D u_texture;

vec3    createCircle(vec2 seed);
float   getAnimatedRadiusFactor();
float   getCircleAlpha(vec3 circle);
float   getAmountOfCircles();
vec2    getRadiusRange();
float   getCircleFade(float alphaIn);

// helper functions
bool    isModePreIntro();
bool    isModeIntro();
bool    isModeOutro();
float   getIntroModeAnimProgress();
float   getStandardModeAnimProgress();
float   getOutroModeAnimProgress();
float   getPassedEffectTime();
float   getRemainingEffectDuration();
float   getDistance(vec2 circleCenter);
float   rand(vec2 seed);
vec2    rand2(vec2 seed);
vec2    convertNormalizedToScreen(vec2 normalized);

void main()
{
    // prevent LibGDX from throwing "uniform not used" exception
    float time = u_time;
    texture2D(u_texture, v_texCoords);
    
    float fragAlpha;
    for (int i = 0; i < getAmountOfCircles(); ++i)
    {
        fragAlpha += (u_paparazziColor.a - 0.2) * getCircleAlpha(createCircle(u_paparazziSeed + i * SEED_STEP_A));
    }
    
    float finalAlpha = getCircleFade(clamp(fragAlpha, 0.0, u_paparazziColor.a));
    
    gl_FragColor = vec4(u_paparazziColor.r, u_paparazziColor.g, u_paparazziColor.b, finalAlpha);
}

// returns deterministic circle property
vec3 createCircle(vec2 seed)
{
    // create circle coords
    vec2 circleCoords = convertNormalizedToScreen(rand2(seed));
    // create radius
    vec2 radiusRange = getRadiusRange();
    float radiusInBounds = rand(seed + SEED_STEP_B) * (radiusRange.y - radiusRange.x) + radiusRange.x;
    radiusInBounds *= getAnimatedRadiusFactor();
    // return circle vec3
    return vec3(circleCoords.x, circleCoords.y, radiusInBounds);
}

float getAnimatedRadiusFactor()
{
    if (isModeIntro()) {
        return getIntroModeAnimProgress();
    }
    
    // standard
    return (1.0 + sin(getStandardModeAnimProgress() * M_2PI) * CIRCLE_ANIMATION_GROW_FACTOR);
}

// returns alpha value if gl_FragCoord is inside the circle. 0.0 otherwise.
float getCircleAlpha(vec3 circle)
{   
    vec2 center = circle.xy;
    float radius = circle.z;
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

float getAmountOfCircles()
{
    return 30 * CIRCLE_MAX_AMOUNT_FACTOR * 
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

float getCircleFade(float alphaIn) {

    float distanceToSafeCircleRegionCenter = (getDistance(u_paparazziOverlaySafeCircle.xy) - u_paparazziOverlaySafeCircle.z);
    // anti aliasing of safe center region border
    if (distanceToSafeCircleRegionCenter <= ANTI_ALIASING_THRESHOLD)
    {
        alphaIn = alphaIn * ( distanceToSafeCircleRegionCenter / ANTI_ALIASING_THRESHOLD );
        // continue with fading afterwards
    }

    // Flash in
    if (isModePreIntro())
    {
        return 1.0;
    }

    // Fade flash to normal
    //if(isModeIntro() && false)
    //{
        //return alphaIn + (1.0 - alphaIn) * (1.0 - (((u_effectDuration - getRemainingEffectDuration()) - PRE_INTRO_DURATION) / INTRO_DURATION));
    //}
    
    // Fade in
    //if(u_effectDuration - getRemainingEffectDuration() <= INTRO_DURATION)
    //{
    //    return alphaIn * (u_effectDuration - getRemainingEffectDuration()) * 4;
    //}
    
    // Fade out
    if(isModeOutro())
    {
        return alphaIn * (1 - getOutroModeAnimProgress());
    }
    
    // exclude safe circle region from overlay
    if (distanceToSafeCircleRegionCenter <= 0.0)
    {
        // no overlay in safe circle region
        return 0.0; 
    }
    
    // Default
    return alphaIn;
}

// helper functions
bool isModePreIntro()
{
    return ( getPassedEffectTime() < (PRE_INTRO_DURATION) );
}

bool isModeIntro()
{
    return ( getPassedEffectTime() >= PRE_INTRO_DURATION ) && ( getPassedEffectTime() < (PRE_INTRO_DURATION + INTRO_DURATION) );
}

bool isModeOutro()
{
   return  ( getPassedEffectTime() >= (u_effectDuration - OUTRO_DURATION) );
}

float getIntroModeAnimProgress()
{
    // calculates (currentEffectTime) / effectTimeMax
    return (getPassedEffectTime() - PRE_INTRO_DURATION) / INTRO_DURATION;
}

float getStandardModeAnimProgress()
{
    // calculates (currentEffectTime) / effectTimeMax
    return (getPassedEffectTime() - PRE_INTRO_DURATION - INTRO_DURATION) / (u_effectDuration - PRE_INTRO_DURATION - INTRO_DURATION - OUTRO_DURATION);
}

float getOutroModeAnimProgress()
{
    // calculates (currentEffectTime) / effectTimeMax
    return (getPassedEffectTime() - (u_effectDuration - OUTRO_DURATION)) / OUTRO_DURATION;
}

float getPassedEffectTime()
{
    return (u_effectDuration - getRemainingEffectDuration());
}

float getRemainingEffectDuration()
{
    // negative remaining duration are set to 0.0
    return max(u_remainingEffectDuration, 0.0);
}

// returns distance between circle center and current gl_FragCoord
float getDistance(vec2 circleCenter)
{
    return length(gl_FragCoord.xy - circleCenter);
}

// returns [0.0, 1.0]
float rand(vec2 seed)
{
    return abs(fract(sin(dot(seed.xy, vec2(12.9898,78.233))) * 43758.5453));
}

// returns vec2([0.0, 1.0], [0.0, 1.0])
vec2 rand2(vec2 seed)
{
    return vec2(rand(seed), rand(seed + SEED_STEP_C));
}

// converts from ([0.0, 1.0], [0.0, 1.0]) to (Width, Height)
vec2 convertNormalizedToScreen(vec2 normalized)
{
    return normalized * u_frameDimension;
}