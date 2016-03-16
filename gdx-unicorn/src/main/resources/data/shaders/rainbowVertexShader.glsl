attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform vec2 u_frameDimension; 
uniform bool u_rainbowMode;
uniform float u_rainbowAlpha;
uniform float u_rainbowFrequency;
uniform float u_rainbowAmplitude;
uniform mat4 u_projTrans;
uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoords;

vec4 calcRainbowColor();
float getColorOffset();

void main()
{
	if(u_rainbowMode)
	{
		vec4 rainbowColor = calcRainbowColor();
		v_color = a_color * (1.0 - u_rainbowAlpha) + rainbowColor * u_rainbowAlpha;
	}
	else
	{
		v_color = a_color;
	}
	v_color.a = v_color.a * (255.0/254.0);
	v_texCoords = a_texCoord0;
	gl_Position =  u_projTrans * a_position;
}

vec4 calcRainbowColor()
{
	vec4 rainbowColor;
	
	float base = getColorOffset() + (a_position.y / u_frameDimension.y);
	float phase = mod(base, 1);
	float level = mod(base, 0.2);
	
	
	if(phase < 0.2)
	{
		rainbowColor = vec4(1.0, level * 5, 0.0, 1.0);
	}
	else if(phase < 0.4)
	{
		rainbowColor = vec4(1.0 - level * 5, 1.0, 0.0, 1.0);
	}
	else if(phase < 0.6)
	{
		rainbowColor = vec4(0.0, 1.0, level * 5, 1.0);
	}
	else if(phase < 0.8)
	{
		rainbowColor = vec4(0.0, 1 - level * 5, 1.0, 1.0);
	}
	else
	{
		rainbowColor = vec4(level * 5, 0.0, 1.0, 1.0);
	}
	
	return rainbowColor;
}

float getColorOffset()
{
	float frequency = u_rainbowFrequency;
	float amplitude = u_rainbowAmplitude;
	if(frequency <= 0.001)
	{
		frequency = 1.0;
	}
	if(amplitude <= 0.001)
	{
		amplitude = 1.0;
	}
	
	return - mod(u_time * u_rainbowFrequency, 1) * amplitude;
	//return (sin(u_time * u_rainbowFrequency) + 1 ) * 0.5 * amplitude;
}