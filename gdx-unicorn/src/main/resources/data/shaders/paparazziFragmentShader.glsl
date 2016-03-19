// author: Daniel Reiners (03/2016)
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

#define M_HALF_PI 1.5707963267948966192313216916398
#define M_PI 3.1415926535897932384626433832795
#define M_2PI 6.283185307179586476925286766559
#define M_3PI 9.4247779607693797153879301498385

// CONSTANTS TO ADJUST SHADER RESULTS
// threshold in px
#define ANTI_ALIASING_THRESHOLD 2.0
// random chosen numbers
#define SEED_STEP 13.0


varying LOWP vec4    v_color;
varying LOWP vec2    v_texCoords;

// standard frame dimensions in unicorn game 1024 x 600
uniform LOWP vec2    u_frameDimension;
uniform LOWP float   u_passedEffectTime;
uniform LOWP float   u_effectDuration;
uniform LOWP vec2    u_paparazziSeed;
// float [0.0, 0.1]
uniform LOWP float   u_paparazziIntensity;
// rgb: color to use for overlay. a: max alpha for result overlay
uniform LOWP vec4    u_paparazziColor;
// xy: circle center screen coordinates. z: circle radius
uniform LOWP vec3    u_paparazziOverlaySafeCircle;

uniform sampler2D u_texture;


varying float   v_circleAmount;
varying vec2    v_radiusRange;
varying float   v_radiusFactor;
// contains animation progress (only active mode >= 0.0)
varying float   v_modePreIntroProgress;
varying float   v_modeIntroProgress;
varying float   v_modeStandardProgress;
varying float   v_modeOutroProgress;

vec3    createCircle(vec2 seed);
float   getCircleAlpha(vec3 circle);
float   getCircleFade(float alphaIn);

// helper functions
float   getDistance(vec2 circleCenter);
vec3    rand3(vec2 seed); // very simple random function
vec2    convertNormalizedToScreen(vec2 normalized);

// prevent LibGDX from throwing "uniform not used" exception
float   dummy(sampler2D);

void main()
{
    // prevent LibGDX from throwing "uniform not used" exception
    dummy(u_texture);
    
    float fragAlpha;
    for (int i = 0; i < v_circleAmount; ++i)
    {
        vec3 currentCircle = createCircle(u_paparazziSeed + i * SEED_STEP);
        if (getDistance(currentCircle.xy) <= currentCircle.z)
        {
            fragAlpha += (u_paparazziColor.a - 0.2) * getCircleAlpha(currentCircle);
        }
    }
    
    float finalAlpha = getCircleFade(clamp(fragAlpha, 0.0, u_paparazziColor.a));
    
    gl_FragColor = vec4(u_paparazziColor.r, u_paparazziColor.g, u_paparazziColor.b, finalAlpha);
}

// returns deterministic circle property
vec3 createCircle(vec2 seed)
{
    vec3 randomNumbers = rand3(seed);
    // create circle coords
    vec2 circleCoords = convertNormalizedToScreen(randomNumbers.xy);
    // create radius
    float radiusInBounds = randomNumbers.z * (v_radiusRange.y - v_radiusRange.x) + v_radiusRange.x;
    radiusInBounds *= v_radiusFactor;
    // return circle vec3
    return vec3(circleCoords, radiusInBounds);
}



// returns alpha value if gl_FragCoord is inside the circle. 0.0 otherwise.
// blends value from center to border.
float getCircleAlpha(vec3 circle)
{
    // circle.z = radius of circle
    float centerDistance = getDistance(circle.xy);
    // inside the circle
    if (centerDistance < circle.z)
    {
        // blend from center to border with cosine
        return cos( (centerDistance / circle.z) * M_HALF_PI );
    }
    // anti aliasing not needed if blending with sine
    
    // VERSION 1: CIRCLE COMPLETELY FILLED
    // inside the circle
    //if (centerDistance < (circle.z - ANTI_ALIASING_THRESHOLD))
    //{
    //    return 1.0;
    //}
    // circle border (anti aliasing)
    //if (centerDistance <= circle.z)
    //{
    //    return ((circle.z - centerDistance) / ANTI_ALIASING_THRESHOLD);
    //}
    
    // outside the circle
    return 0.0;
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
    if (v_modePreIntroProgress >= 0.0)
    {
        return 1.0;
    }
    
    // Fade out
    if (v_modeOutroProgress >= 0.0)
    {
        return alphaIn * (1.0 - v_modeOutroProgress);
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


// returns distance between circle center and current gl_FragCoord
float getDistance(vec2 circleCenter)
{
    return length(gl_FragCoord.xy - circleCenter);
}

// returns vec3([0.0, 1.0], [0.0, 1.0], [0.0, 1.0])
vec3 rand3(vec2 seed)
{
    float random = abs(sin(dot(seed.xy, vec2(12.9898,78.233))) * 437.5453);
    return vec3(
        fract(random),
        fract(random * seed.x * M_PI),
        fract(random * seed.y * M_2PI)
        );
}

// converts from ([0.0, 1.0], [0.0, 1.0]) to (Width, Height)
vec2 convertNormalizedToScreen(vec2 normalized)
{
    return normalized * u_frameDimension;
}
// prevent LibGDX from throwing "uniform not used" exception
float dummy(sampler2D) { return 0.0; }