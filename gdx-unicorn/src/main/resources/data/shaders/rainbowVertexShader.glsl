#define M_2PI 6.2831843071795864777252867665590
#define PART 0.1666666666666666666666666666666

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform vec2 u_frameDimension;
uniform float u_startDuration;
uniform float u_durationLeft;
uniform float u_rainbowAlpha;
uniform float u_rainbowFrequency;
uniform float u_rainbowAmplitude;
uniform mat4 u_projTrans;
uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoords;

vec4 calcRainbowColor();
float getHorizontalOffset();
float getColorOffset();
float getRainbowAlpha();

void main()
{
	vec4 rainbowColor = calcRainbowColor();
	float alpha = getRainbowAlpha();
	v_color = a_color * (1.0 - alpha) + rainbowColor * alpha;
	v_color.a = v_color.a * (255.0/254.0);
	v_texCoords = a_texCoord0;
	gl_Position =  u_projTrans * a_position;
}

vec4 calcRainbowColor()
{
	vec4 rainbowColor;
	
	float colorOffset = getColorOffset();
	float base = getHorizontalOffset() + (a_position.y / u_frameDimension.y);
	float phase = mod(base, 1);
	float level = mod(base, PART);
	
	if(phase < PART)
	{
		rainbowColor = vec4(1.0, level * 6, 0.0, 1.0);
	}
	else if(phase < PART * 2)
	{
		rainbowColor = vec4(1.0 - level * 6, 1.0, 0.0, 1.0);
	}
	else if(phase < PART * 3)
	{
		rainbowColor = vec4(0.0, 1.0, level * 6, 1.0);
	}
	else if(phase < PART * 4)
	{
		rainbowColor = vec4(0.0, 1 - level * 6, 1.0, 1.0);
	}
	else if(phase < PART * 5)
	{
		rainbowColor = vec4(level * 6, 0.0, 1.0, 1.0);
	}
	else
	{
		rainbowColor = vec4(1.0, 0.0, 1.0 - level * 6, 1.0);
	}
	
	return rainbowColor;
}

float getColorOffset()
{
	float frequency = u_rainbowFrequency;
	
	if(frequency <= 0.001)
	{
		frequency = 1.0;
	}
	
	return - mod(u_time * u_rainbowFrequency, 1);
}

float getHorizontalOffset()
{
	float amplitude = u_rainbowAmplitude;
	if(amplitude <= 0.001)
	{
		amplitude = 0.25;
	}
	return sin(M_2PI * (a_position.x / u_frameDimension.x) + u_time * u_rainbowFrequency) * amplitude;
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