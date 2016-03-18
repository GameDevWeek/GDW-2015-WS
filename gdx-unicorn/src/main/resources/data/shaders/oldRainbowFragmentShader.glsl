#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif
#define PART 0.1666666666666666666666666666666

varying LOWP vec4 v_color;
varying float v_rainbowAlpha;

uniform vec2 u_frameDimension;
uniform float u_rainbowFrequency;
uniform float u_time;

vec4 calcRainbowColor();
float getColorOffset();

void main()
{
	//vec4 rainbowColor = v_color * (1.0 - v_rainbowAlpha) + calcRainbowColor() * v_rainbowAlpha;
	
	//gl_FragColor = rainbowColor * texture2D(u_texture, v_texCoords);
	gl_FragColor = calcRainbowColor();
	gl_FragColor.a = v_rainbowAlpha;
}

vec4 calcRainbowColor()
{	
	vec4 rainbowColor;
	
	float base = getColorOffset() + (gl_FragCoord.y / u_frameDimension.y);
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
	
	return mod(u_time * u_rainbowFrequency, 1);
}