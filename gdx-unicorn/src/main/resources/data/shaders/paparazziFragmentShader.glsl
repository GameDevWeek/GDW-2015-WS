#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

#define ANTI_ALIASING_THRESHOLD 2.0

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform float u_startDuration;
uniform float u_durationLeft;
uniform float u_paparazziAlpha;
uniform float u_paparazziIntensity;
uniform vec2 u_frameDimension;
uniform float u_time;

uniform sampler2D u_texture;

float getCircleAlpha(vec2 center, float radius);
vec2 getCircleCenter(float seed);
float getCircleRadius(float seed);

void main()
{
    //DUMMY
    vec2 d1 = u_frameDimension;
    float d2 = u_paparazziIntensity;
    float d3 = u_paparazziAlpha;
    float time = u_time;
    texture2D(u_texture, v_texCoords);
    
    // test 1 circle
    vec2 center = vec2( u_frameDimension.x / 2, u_frameDimension.y / 2 );
    float radius = 200.0;
    
	gl_FragColor = vec4(1.0, 1.0, 1.0, getCircleAlpha(center, radius));
}

float getCircleAlpha(vec2 center, float radius)
{   
    
    vec2 fragPos = vec2(gl_FragCoord.x, gl_FragCoord.y);
    float distance = length(fragPos - center);
    
    float fragAlpha;
    if (distance < (radius - ANTI_ALIASING_THRESHOLD))
    {
        fragAlpha = 1.0;
    }
    else if (distance < radius)
    {
        fragAlpha = ((radius - distance) / ANTI_ALIASING_THRESHOLD);
    }
    else 
    {
        fragAlpha = 0.0;
    }

    // Fade in
    if(u_startDuration - u_durationLeft <= 0.25)
    {
        return fragAlpha * (u_startDuration - u_durationLeft) * 4;
    }
    
    // Fade out
    if(u_durationLeft <= 0.5)
    {
        return fragAlpha * u_durationLeft * 2;
    }
    
    // Default
    return fragAlpha;
}

vec2 getCircleCenter(float seed)
{
    return vec2(1.0, 1.0);
}

float getCircleRadius(float seed)
{
    return 1.0;
}