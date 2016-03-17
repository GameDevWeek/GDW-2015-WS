#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

// threshold in px
#define ANTI_ALIASING_THRESHOLD 2.0
// random chosen number
#define SEED_STEP 78.1563
// duration in seconds
#define FLASH_DURATION 0.1
#define CIRCLE_ANIMATION_GROW_FACTOR 0.4


varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform float   u_startDuration;
uniform float   u_durationLeft;
uniform vec2    u_paparazziSeed;
uniform float   u_paparazziIntensity;
uniform vec3    u_paparazziColor;
uniform vec2    u_paparazziCircleRadiusRange;
uniform vec2    u_frameDimension;
uniform float   u_time;

uniform sampler2D u_texture;

float   getCircleAlpha(vec2 center, float radius);
vec2    getCircleCenter(vec2 seed);
float   getCircleRadius(vec2 seed);

float   fade(float alpha); // when to fade is set in function

// helper functions
float   rand(vec2 seed);
vec2    rand2(vec2 seed);

float   lerp(float a, float b, float t);
float   getAnimProgress();

vec2    normalizedToScreen(vec2 normalized);

void main()
{
    // prevent LibGDX from throwing "uniform not used" exception
    vec2 d1 = u_frameDimension;
    float time = u_time;
    texture2D(u_texture, v_texCoords);
    
    float fragAlpha;
    for (int i = 0; i < (u_paparazziIntensity * 2); ++i)
    {
        fragAlpha += 0.8 * getCircleAlpha(getCircleCenter(u_paparazziSeed + i * SEED_STEP), getCircleRadius(u_paparazziSeed + i * SEED_STEP));
    }
    
    float finalAlpha = fade(clamp(fragAlpha, 0.0, 1.0));
    
    gl_FragColor = vec4(u_paparazziColor.r, u_paparazziColor.g, u_paparazziColor.b, finalAlpha);
}

vec2 getCircleCenter(vec2 seed)
{
    return normalizedToScreen(rand2(seed));
}

float getCircleRadius(vec2 seed)
{
    // vec2 u_paparazziCircleRadiusRange(minRadius, maxRadius)
    float radiusInBounds = rand(seed + 87.16854) * (u_paparazziCircleRadiusRange.y - u_paparazziCircleRadiusRange.x) + u_paparazziCircleRadiusRange.x;
    return radiusInBounds *= (1.0 + getAnimProgress() * CIRCLE_ANIMATION_GROW_FACTOR);
}

float getCircleAlpha(vec2 center, float radius)
{   
    vec2 fragPos = vec2(gl_FragCoord.x, gl_FragCoord.y);
    float distance = length(fragPos - center);
    
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

float fade(float alpha) {
    // Flash in
    if (u_startDuration - u_durationLeft <= FLASH_DURATION)
    {
        return 1.0;
    }

    // Fade flash to normal
    if(u_startDuration - u_durationLeft <= FLASH_DURATION * 2)
    {
        return alpha + (1.0 - alpha) * (1.0 - (((u_startDuration - u_durationLeft) - FLASH_DURATION) / FLASH_DURATION));
    }
    
    // Fade in
    //if(u_startDuration - u_durationLeft <= 0.25)
    //{
    //    return alpha * (u_startDuration - u_durationLeft) * 4;
    //}
    
    // Fade out
    if(u_durationLeft <= 0.5)
    {
        return alpha * u_durationLeft * 2;
    }
    
    // Default
    return alpha;
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

// returns vec2([0.0, 1.0], [0.0], [1.0])
vec2 rand2(vec2 seed)
{
    return vec2(rand(seed), rand(seed + 59.742));
}

vec2 normalizedToScreen(vec2 normalized)
{
    return normalized * u_frameDimension;
}