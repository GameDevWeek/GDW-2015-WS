// author: Daniel Reiners (03/2016)

#define M_HALF_PI 1.5707963267948966192313216916398
#define M_PI 3.1415926535897932384626433832795
#define M_2PI 6.283185307179586476925286766559
#define M_3PI 9.4247779607693797153879301498385

// CONSTANTS TO ADJUST SHADER RESULTS
#define CIRCLE_RADIUS_ANIMATION_GROW_FACTOR 0.1
// lerps from start min to end min, ...
#define CIRCLE_RADIUS_RANGE_FACTOR_START_MIN 0.5 
#define CIRCLE_RADIUS_RANGE_FACTOR_START_MAX 1.0 
#define CIRCLE_RADIUS_RANGE_FACTOR_END_MIN 2.2
#define CIRCLE_RADIUS_RANGE_FACTOR_END_MAX 3.0
// duration in seconds
#define PRE_INTRO_DURATION 0.1
#define INTRO_DURATION 0.2
#define OUTRO_DURATION 0.4

attribute vec4  a_position;
attribute vec4  a_color;
attribute vec2  a_texCoord0;

uniform vec2    u_frameDimension;
uniform mat4    u_projTrans;

uniform float   u_passedEffectTime;
uniform float   u_effectDuration;
uniform float   u_paparazziIntensity;

varying float   v_circleAmount;
varying vec2    v_radiusRange;
varying float   v_radiusFactor;
// contains animation progress (only active mode >= 0.0)
varying float   v_modePreIntroProgress;
varying float   v_modeIntroProgress;
varying float   v_modeStandardProgress;
varying float   v_modeOutroProgress;

// functions
vec2    getRadiusRange();
float   getAmountOfCircles();

// helper functions
float   lerp(float a, float b, float t);
// TIME helper functions
vec4    getAnimModesProgress();

void main()
{
    // prevent LibGDX from throwing "uniform not used" exception
    mat4 dummy1 = u_projTrans;
    
    // move from center to lower left corner
	gl_Position =  vec4(a_position.x - u_frameDimension.x/2, a_position.y - u_frameDimension.y/2, a_position.z, a_position.w);
	
	v_circleAmount = getAmountOfCircles();
	v_radiusRange = getRadiusRange();
	
	// calculates animation mode progresses
	// pre intro mode
    if (u_passedEffectTime < PRE_INTRO_DURATION)
    {
        v_modePreIntroProgress = u_passedEffectTime / PRE_INTRO_DURATION;
        v_modeIntroProgress = -1.0;
        v_modeStandardProgress = -1.0;
        v_modeOutroProgress = -1.0;
    }
    // intro mode
    else if ( ( u_passedEffectTime >= PRE_INTRO_DURATION ) && ( u_passedEffectTime < (PRE_INTRO_DURATION + INTRO_DURATION) ))
    {
        v_modePreIntroProgress = -1.0;
        v_modeIntroProgress = (u_passedEffectTime - PRE_INTRO_DURATION) / INTRO_DURATION;
        v_modeStandardProgress = -1.0;
        v_modeOutroProgress = -1.0;
    }
    // outro mode
    else if ( u_passedEffectTime >= (u_effectDuration - OUTRO_DURATION) )
    {
        v_modePreIntroProgress = -1.0;
        v_modeIntroProgress = -1.0;
        v_modeStandardProgress = -1.0;
        v_modeOutroProgress = (u_passedEffectTime - (u_effectDuration - OUTRO_DURATION)) / OUTRO_DURATION;
    }
    // standard mode
    else
    {
        v_modePreIntroProgress = -1.0;
        v_modeIntroProgress = -1.0;
        v_modeStandardProgress = (u_passedEffectTime - PRE_INTRO_DURATION - INTRO_DURATION) / (u_effectDuration - PRE_INTRO_DURATION - INTRO_DURATION - OUTRO_DURATION);
        v_modeOutroProgress = -1.0;
    }
	
	// calculates v_radiusFactor
	if (v_modeIntroProgress >= 0.0)
	{
        // intro mode
        v_radiusFactor =  sin(v_modeIntroProgress * M_HALF_PI);
    }
    else
    {
        // all other modes
        v_radiusFactor = (1.0 + sin(v_modeStandardProgress * M_PI) * CIRCLE_RADIUS_ANIMATION_GROW_FACTOR);
    }

}

// returns vec2(minRadius, maxRadius)
vec2 getRadiusRange()
{
    float baseFactor = max(u_frameDimension.x, u_frameDimension.y) * 0.0625; // 1/16
    // u_paparazziIntensity
    float rangeMin = lerp(CIRCLE_RADIUS_RANGE_FACTOR_START_MIN, CIRCLE_RADIUS_RANGE_FACTOR_END_MIN, u_paparazziIntensity);
    float rangeMax = lerp(CIRCLE_RADIUS_RANGE_FACTOR_START_MAX, CIRCLE_RADIUS_RANGE_FACTOR_END_MAX, u_paparazziIntensity);
    
    return vec2(baseFactor * rangeMin, baseFactor * rangeMax);
}

float getAmountOfCircles()
{
    return 20 * 
        (
            // u_paparazziIntensity is taken 40 percent into account
            (1 - clamp(u_paparazziIntensity, 0.0, 1.0)) * 0.4 
            + 0.6
        );
} 

// lerps from a [t = 0] to b [t = 1]
float lerp(float a, float b, float t)
{
    return a * (1 - t) + b * t;
}