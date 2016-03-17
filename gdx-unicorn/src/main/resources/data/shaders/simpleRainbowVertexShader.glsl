attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform float u_rainbowAlpha;
uniform float u_startDuration;
uniform float u_durationLeft;
uniform mat4 u_projTrans;

varying vec4 v_color;
varying float v_rainbowAlpha;
varying vec2 v_texCoords;

float getRainbowAlpha();

void main()
{
	v_rainbowAlpha = getRainbowAlpha();
	v_color = vec4(1.0, 1.0, 1.0, 1.0);
	v_texCoords = a_texCoord0;
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