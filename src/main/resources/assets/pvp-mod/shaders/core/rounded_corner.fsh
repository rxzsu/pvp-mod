#version 330

layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
    mat4 TextureMat;
};

in vec4 vertexColor;
in vec2 localPx;

out vec4 fragColor;

// Четверть-скругление: дуга с центром в (RADIUS, RADIUS) локальных пикселей.
// Пиксели за дугой (ближе к углу) прозрачные, у дуги - сглаживание.
void main() {
    float d = distance(localPx, vec2(RADIUS));
    float alpha = 1.0 - smoothstep(RADIUS - 1.0, RADIUS, d);

    vec4 color = vertexColor;
    if (color.a == 0.0 || alpha <= 0.0) {
        discard;
    }
    fragColor = vec4(color.rgb, color.a * alpha) * ColorModulator;
}
