attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform vec2 u_frameDimension;
uniform float u_startDuration;
uniform float u_durationLeft;
uniform float u_paparazziAlpha;
uniform float u_paparazziIntensity;
uniform mat4 u_projTrans;
uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoords;

float getAlpha();

void main()
{
    //DUMMY
    vec2 d1 = u_frameDimension;
    float d2 = u_paparazziIntensity;
	float alpha = getAlpha();
	float time = u_time;
	//v_color = a_color * (1.0 - alpha) + vec4(1.0, 1.0, 1.0, 1.0) * alpha;
	v_color = vec4(1.0,0.0,0.0,1.0);
	v_color.a = v_color.a * (255.0/254.0);
	v_texCoords = a_texCoord0;
	gl_Position =  u_projTrans * a_position;
}

float getAlpha()
{
	// Fade in
	if(u_startDuration - u_durationLeft <= 0.25)
	{
		return u_paparazziAlpha * (u_startDuration - u_durationLeft) * 4;
	}
	
	// Fade out
	if(u_durationLeft <= 0.5)
	{
		return u_paparazziAlpha * u_durationLeft * 2;
	}
	
	// Default
	return u_paparazziAlpha;
}