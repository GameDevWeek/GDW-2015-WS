attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;


uniform vec2 u_frameDimension;
uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
    // prevent LibGDX from throwing "uniform not used" exception
    mat4 dummy1 = u_projTrans;
    
    v_color = v_color;
	v_color.a = v_color.a * (255.0/254.0);
    v_texCoords = a_texCoord0;
    // move from center to lower left corner
	gl_Position =  vec4(a_position.x - u_frameDimension.x/2, a_position.y - u_frameDimension.y/2, a_position.z, a_position.w);
}