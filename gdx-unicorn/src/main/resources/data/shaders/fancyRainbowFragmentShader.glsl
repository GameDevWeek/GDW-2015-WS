#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif
#define M_2PI 6.2831843071795864777252867665590
#define PART 0.1666666666666666666666666666666

varying float v_rainbowAlpha;

uniform vec2 u_frameDimension;
uniform float u_rainbowFrequency;
uniform float u_rainbowAmplitude;
uniform float u_time;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

vec4 calcRainbowColor();
float getHorizontalOffset();

void main()
{
	// use uniforms so they do not get removed
	vec4 tex = u_projTrans * texture2D(u_texture, a_texCoord0);
	//vec4 rainbowColor = v_color * (1.0 - v_rainbowAlpha) + calcRainbowColor() * v_rainbowAlpha;
	
	gl_FragColor = calcRainbowColor();
	gl_FragColor.a = v_rainbowAlpha;
	//gl_FragColor = (calcRainbowColor() * v_rainbowAlpha) + (texture2D(u_texture, v_texCoords) * (1.0 - v_rainbowAlpha));
}

vec4 calcRainbowColor()
{
	vec4 rainbowColor;
	
	float base = getHorizontalOffset() + (gl_FragCoord.y / u_frameDimension.y);
	float phase = mod(base, 1);
	float level = mod(base, PART);
	
	if(phase <= PART)
	{
		rainbowColor = vec4(1.0, level * 6, 0.0, 1.0);
	}
	else if(phase <= PART * 2)
	{
		rainbowColor = vec4(1.0 - level * 6, 1.0, 0.0, 1.0);
	}
	else if(phase <= PART * 3)
	{
		rainbowColor = vec4(0.0, 1.0, level * 6, 1.0);
	}
	else if(phase <= PART * 4)
	{
		rainbowColor = vec4(0.0, 1 - level * 6, 1.0, 1.0);
	}
	else if(phase <= PART * 5)
	{
		rainbowColor = vec4(level * 6, 0.0, 1.0, 1.0);
	}
	else
	{
		rainbowColor = vec4(1.0, 0.0, 1.0 - level * 6, 1.0);
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