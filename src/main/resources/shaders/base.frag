#version 400 core

in vec3 color;
out vec4 pixel;


void main(void) {
    pixel = vec4(color, 1.0);
}
