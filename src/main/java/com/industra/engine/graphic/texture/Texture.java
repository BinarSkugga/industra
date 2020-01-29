/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.texture;

import com.industra.engine.graphic.Texturable;
import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Texture implements Texturable {
    private static final int COLOR_SIZE = 4;
    private static int LAST_BIND = 0;

    @Getter private int id;
    @Getter private String name;
    @Getter private Vector2i imageSize;
    @Getter private Vector2f size;

    public Texture(@NonNull String name, @NonNull TextureInterpolation interpolation) {
        try {
            this.name = name;

            InputStream textureStream = Texture.class.getClassLoader().getResourceAsStream("textures/" + name + ".png");
            PNGDecoder decoder = new PNGDecoder(textureStream);
            this.imageSize = new Vector2i(decoder.getWidth(), decoder.getHeight());

            ByteBuffer buffer = BufferUtils.createByteBuffer(this.imageSize.x * this.imageSize.y * COLOR_SIZE);
            decoder.decode(buffer, this.imageSize.x * COLOR_SIZE, PNGDecoder.Format.RGBA);
            buffer.flip();

            this.id = GL11.glGenTextures();
            this.bind();

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, interpolation.value());
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, interpolation.value());

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.imageSize.x, this.imageSize.y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            this.unbind();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Texture(@NonNull String name) {
        this(name, TextureInterpolation.NEAREST);
    }

    @Override
    public void bind() {
        if(this.id != LAST_BIND) {
            LAST_BIND = this.id;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
        }
    }

    @Override
    public void unbind() {
        LAST_BIND = 0;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void dispose() {
        GL11.glDeleteTextures(this.id);
    }
}
