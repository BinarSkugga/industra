#version 400 core

in vec2 position;
in vec2 texCoords;

out vec2 out_texCoords;
uniform mat4 texTransformation;

uniform mat4 projection;
uniform mat4 transformation;

void main(void) {
    gl_Position = projection * transformation * vec4(position, 1.0, 1.0);
    out_texCoords = (texTransformation * vec4(texCoords, 1.0, 1.0)).xy;
}
