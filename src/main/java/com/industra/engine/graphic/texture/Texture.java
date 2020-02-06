/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.texture;

import com.industra.engine.graphic.Animatable;
import com.industra.engine.graphic.Texturable;
import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Texture implements Texturable, Animatable {
    private static final int COLOR_SIZE = 4;
    private static int LAST_BIND = 0;

    @Getter private int id;
    @Getter private String name;
    @Getter private Vector2i imageSize;

    @Getter private Vector2f animationSize;
    @Getter private Vector2f scale;
    @Getter private Vector2f position;

    @Getter @Setter private boolean animated = false;
    @Getter @Setter private long lastFrameTime = 0;
    @Getter @Setter private int frame = 0;

    public Texture(@NonNull String name, @NonNull TextureInterpolation interpolation) {
        try {
            this.name = name;

            InputStream textureStream = Texture.class.getClassLoader().getResourceAsStream("textures/" + name + ".png");
            PNGDecoder decoder = new PNGDecoder(textureStream);
            this.imageSize = new Vector2i(decoder.getWidth(), decoder.getHeight());

            this.position = new Vector2f(0f, 0f);
            this.scale = new Vector2f(this.imageSize);
            this.animationSize = new Vector2f(this.scale);

            ByteBuffer buffer = BufferUtils.createByteBuffer(this.imageSize.x * this.imageSize.y * COLOR_SIZE);
            decoder.decode(buffer, this.imageSize.x * COLOR_SIZE, PNGDecoder.Format.RGBA);
            buffer.flip();

            this.id = GL11.glGenTextures();
            this.bind();

            GL31.glTexParameteri(GL31.GL_TEXTURE_RECTANGLE, GL31.GL_TEXTURE_WRAP_S, GL31.GL_CLAMP_TO_EDGE);
            GL31.glTexParameteri(GL31.GL_TEXTURE_RECTANGLE, GL31.GL_TEXTURE_WRAP_T, GL31.GL_CLAMP_TO_EDGE);

            GL31.glTexParameteri(GL31.GL_TEXTURE_RECTANGLE, GL31.GL_TEXTURE_MIN_FILTER, interpolation.value());
            GL31.glTexParameteri(GL31.GL_TEXTURE_RECTANGLE, GL31.GL_TEXTURE_MAG_FILTER, interpolation.value());

            GL31.glTexImage2D(GL31.GL_TEXTURE_RECTANGLE, 0, GL31.GL_RGBA, this.imageSize.x, this.imageSize.y, 0, GL31.GL_RGBA, GL31.GL_UNSIGNED_BYTE, buffer);
            this.unbind();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Texture(@NonNull String name) {
        this(name, TextureInterpolation.NEAREST);
    }

    @Override
    public Vector3f rotation() {
        return new Vector3f(0, 0, 0);
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

    @Override
    public Matrix4f texCoordTransformation() {
        if(this.animated) {
            return this.frameTransformation(50);
        } else {
            return this.transformation();
        }
    }
}
