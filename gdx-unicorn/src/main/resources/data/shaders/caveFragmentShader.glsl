// author: Daniel Reiners (03/2016)

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

uniform float   u_caveIntensity;
uniform float   u_lightGlowIntensity;
uniform float   u_lightGlowRadius;
uniform vec4    u_lightColor;
uniform vec4    u_darknessColor;

uniform sampler2D   u_texture;

varying float   u_numOfLights;
varying vec4    v_lightSource0;
varying vec4    v_lightSource1;
varying vec4    v_lightSource2;
varying vec4    v_lightSource3;
varying vec4    v_lightSource4;
varying vec4    v_lightSource5;
varying vec4    v_lightSource6;
varying vec4    v_lightSource7;
varying vec4    v_lightSource8;
varying vec4    v_lightSource9;
varying vec4    v_lightSource10;
varying vec4    v_lightSource11;
varying vec4    v_lightSource12;
varying vec4    v_lightSource13;
varying vec4    v_lightSource14;

//functions


// helper functions
vec4 lerp4(vec4 a, vec4 b, float t);

void main()
{
    // prevent LibGDX from throwing "uniform not used" exception
    texture2D(u_texture, u_lightColor.rg);
    
    // only darken
    float intensityAlpha = u_darknessColor.a * u_caveIntensity;
	gl_FragColor = vec4(u_darknessColor.rgb, intensityAlpha);
}

// lerps from a [t = 0] to b [t = 1]
vec4 lerp4(vec4 a, vec4 b, float t)
{
    return a * (1 - t) + b * t;
}