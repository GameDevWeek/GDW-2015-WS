attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform vec2 u_frameDimension;
uniform float u_rainbowAlpha;
uniform float u_rainbowAmplitude;
uniform float u_startDuration;
uniform float u_durationLeft;
uniform mat4 u_projTrans;
uniform sampler2D u_texture;

varying vec4 v_color;
varying float v_rainbowAlpha;

float getRainbowAlpha();

void main()
{
	// use uniforms so they do not get removed
	texture2D(u_texture, a_texCoord0);
	float amp = u_rainbowAmplitude;
	
	v_rainbowAlpha = getRainbowAlpha();
	gl_Position =  u_projTrans * a_position;
}

float getRainbowAlpha()
{
	// Fade rainbow in
	if(u_startDuration - u_durationLeft <= 0.25)
	{
		return u_rainbowAlpha * (u_startDuration - u_durationLeft) * 4;
	}
	
	// Fade rainbow out
	if(u_durationLeft <= 0.5)
	{
		return u_rainbowAlpha * u_durationLeft * 2;
	}
	
	// Default
	return u_rainbowAlpha;
}