#version 400 core

in vec2 out_texCoord;
out vec4 pixel;

uniform sampler2D texture;

void main(void) {
    pixel = texture2D(texture, out_texCoord);
}
