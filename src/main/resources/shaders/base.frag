#version 400

in vec2 out_texCoord;
out vec4 pixel;

uniform sampler2DRect texture;

void main(void) {
    pixel = texture2DRect(texture, out_texCoord);
}
