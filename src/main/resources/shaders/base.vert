#version 400

in vec2 position;
in vec2 texCoord;

out vec2 out_texCoord;
uniform mat4 texCoordTransformation;

uniform mat4 projection;
uniform mat4 transformation;

void main(void) {
    gl_Position = projection * transformation * vec4(position, 1.0, 1.0);
    out_texCoord = (texCoordTransformation * vec4(texCoord, 1.0, 1.0)).xy;
}
