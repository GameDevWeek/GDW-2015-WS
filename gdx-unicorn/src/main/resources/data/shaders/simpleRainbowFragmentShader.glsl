#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif
#define M_2PI 6.2831843071795864777252867665590
#define PART 0.2
#define BLEND_PIXEL 3

varying LOWP vec4 v_color;
varying float v_rainbowAlpha;

uniform vec2 u_frameDimension;
uniform float u_rainbowFrequency;
uniform float u_rainbowAmplitude;
uniform float u_time;

vec4 calcRainbowColor();
float getHorizontalOffset();

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
	
	float blend = BLEND_PIXEL / u_frameDimension.y;
	float oBlend = 1.0 / blend;
	
	float base = getHorizontalOffset() + (gl_FragCoord.y / u_frameDimension.y);
	float phase = mod(base, 1);
	
	float level = mod(base, blend);
	
	if(phase <= PART - blend)
	{
		// Full red
		rainbowColor = vec4(1.0, 0.0, 0.0, 1.0);
	}
	else if(phase <= PART)
	{
		// RELATIVE_BLEND between red and yellow
		rainbowColor = vec4(1.0, oBlend * level, 0.0, 1.0);
	}
	else if(phase <= PART * 2 - blend)
	{
		// Full yellow
		rainbowColor = vec4(1.0, 1.0, 0.0, 1.0);
	}
	else if(phase <= PART * 2)
	{
		// RELATIVE_BLEND between yellow and green
		rainbowColor = vec4(1.0 - oBlend * level, 1.0, 0.0, 1.0);
	}
	else if(phase <= PART * 3 - blend)
	{
		// Full green
		rainbowColor = vec4(0.0, 1.0, 0.0, 1.0);
	}
	else if(phase <= PART * 3)
	{
		// RELATIVE_BLEND between green and cyan
		rainbowColor = vec4(0.0, 1.0, oBlend * level, 1.0);
	}
	else if(phase <= PART * 4 - blend)
	{
		// Full cyan
		rainbowColor = vec4(0.0, 1.0, 1.0, 1.0);
	}
	else if(phase <= PART * 4)
	{
		// RELATIVE_BLEND between cyan and blue
		rainbowColor = vec4(0.0, 1.0 - oBlend * level, 1.0, 1.0);
	}
	else if(phase <= PART * 5 - blend)
	{
		// Full blue
		rainbowColor = vec4(0.0, 0.0, 1.0, 1.0);
	}
	else
	{
		// RELATIVE_BLEND between blue and red
		rainbowColor = vec4(oBlend * level, 0.0, 1.0 - oBlend * level, 1.0);
	}
	
	return rainbowColor;
}

float getHorizontalOffset()
{
	float amplitude = u_rainbowAmplitude;
	if(amplitude <= 0.001)
	{
		amplitude = 0.25;
	}
	return sin(M_2PI * (gl_FragCoord.x / u_frameDimension.x) + u_time * u_rainbowFrequency) * amplitude;
}