// author: Daniel Reiners (03/2016)

attribute vec4  a_position;
attribute vec4  a_color;
attribute vec2  a_texCoord0;

uniform mat4    u_projTrans;

uniform vec2    u_lightSource0;
uniform vec2    u_lightSource1;
uniform vec2    u_lightSource2;
uniform vec2    u_lightSource3;
uniform vec2    u_lightSource4;
uniform vec2    u_lightSource5;
uniform vec2    u_lightSource6;
uniform vec2    u_lightSource7;
uniform vec2    u_lightSource8;
uniform vec2    u_lightSource9;
uniform vec2    u_lightSource10;
uniform vec2    u_lightSource11;
uniform vec2    u_lightSource12;
uniform vec2    u_lightSource13;
uniform vec2    u_lightSource14;

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

void main()
{
    // prevent LibGDX from throwing "not used" exception
    vec4 dummy1 = a_color;
    vec2 dummy2 = a_texCoord0;
    
    // converting light positions into gl screen coordinates
    v_lightSource0 = u_projTrans * vec4(u_lightSource0, 0.0, 1.0);
    v_lightSource1 = u_projTrans * vec4(u_lightSource1, 0.0, 1.0);
    v_lightSource2 = u_projTrans * vec4(u_lightSource2, 0.0, 1.0);
    v_lightSource3 = u_projTrans * vec4(u_lightSource3, 0.0, 1.0);
    v_lightSource4 = u_projTrans * vec4(u_lightSource4, 0.0, 1.0);
    v_lightSource5 = u_projTrans * vec4(u_lightSource5, 0.0, 1.0);
    v_lightSource6 = u_projTrans * vec4(u_lightSource6, 0.0, 1.0);
    v_lightSource7 = u_projTrans * vec4(u_lightSource7, 0.0, 1.0);
    v_lightSource8 = u_projTrans * vec4(u_lightSource8, 0.0, 1.0);
    v_lightSource9 = u_projTrans * vec4(u_lightSource9, 0.0, 1.0);
    v_lightSource10 = u_projTrans * vec4(u_lightSource10, 0.0, 1.0);
    v_lightSource11 = u_projTrans * vec4(u_lightSource11, 0.0, 1.0);
    v_lightSource12 = u_projTrans * vec4(u_lightSource12, 0.0, 1.0);
    v_lightSource13 = u_projTrans * vec4(u_lightSource13, 0.0, 1.0);
    v_lightSource14 = u_projTrans * vec4(u_lightSource14, 0.0, 1.0);
    
    // converting vertex position into gl screen coordinates
	gl_Position =  u_projTrans * a_position;
	//gl_Position = vec4(0.0,0.0,0.0,1.0);
}