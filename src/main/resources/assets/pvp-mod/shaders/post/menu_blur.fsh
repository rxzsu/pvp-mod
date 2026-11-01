#version 330

#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform BlurConfig {
    vec2 BlurDir;
    float Radius;
};

in vec2 texCoord;

out vec4 fragColor;

// Gaussian blur with baked radius + slight desaturation (Lunar-style menu backdrop)
void main() {
    vec2 oneTexel = 1.0 / InSize;
    vec2 sampleStep = oneTexel * BlurDir;

    float sigma = max(Radius, 1.0) / 3.0;
    float weightSum = 0.0;
    vec4 blurred = vec4(0.0);
    for (float a = -Radius; a <= Radius; a += 1.0) {
        float w = exp(-(a * a) / (2.0 * sigma * sigma));
        blurred += texture(InSampler, texCoord + sampleStep * a) * w;
        weightSum += w;
    }
    blurred /= weightSum;

    // slight desaturation so UI panels pop
    float gray = dot(blurred.rgb, vec3(0.299, 0.587, 0.114));
    blurred.rgb = mix(vec3(gray), blurred.rgb, 0.82);

    fragColor = blurred;
}
