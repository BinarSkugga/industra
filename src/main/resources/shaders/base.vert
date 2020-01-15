#version 400 core

in vec2 position;
out vec3 color;


void main(void) {
    gl_Position = vec4(position, 1.0, 1.0);
    color = vec3(1, 0, 0); // Red color
}
