#version 400 core

in vec2 position;
out vec3 color;

uniform mat4 projection;
uniform mat4 transformation;

void main(void) {
    gl_Position = projection * transformation * vec4(position, 1.0, 1.0);
    color = vec3(1, 1, 1);
}
