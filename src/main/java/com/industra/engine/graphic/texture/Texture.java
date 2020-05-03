/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.texture;

import com.industra.engine.graphic.Texturable;
import com.industra.engine.graphic.Transformable;
import com.industra.utils.Clock;
import com.industra.utils.Logger;
import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Texture implements Texturable, Transformable {
    private static final int COLOR_SIZE = 4;
    private static final int FRAME_TIME = 150;
    private static final int RETURN_FRAME_TIME = 50;
    private static int LAST_BIND = 0;

    @Getter private int id;
    @Getter private String name;

    @Getter private Vector2i imageSize;
    @Getter private Vector2f scale;
    @Getter private Vector2f rootPosition;
    @Getter private Vector2f position;
    @Getter private float scaler;

    @Getter @Setter private int defaultFrame = 0;
    @Getter private int totalFrame = 0;
    @Getter private int frame = this.defaultFrame;
    @Getter private long changed = 0;
    @Getter private boolean animated = false;
    @Getter private boolean returning = false;

    public Texture(@NonNull String name, @NonNull TextureInterpolation interpolation) {
        try {
            this.name = name;
            this.rootPosition = new Vector2f(0f, 0f);
            this.position = new Vector2f(0f, 0f);

            InputStream textureStream = Texture.class.getClassLoader().getResourceAsStream("textures/" + name + ".png");
            PNGDecoder decoder = new PNGDecoder(textureStream);
            this.imageSize = new Vector2i(decoder.getWidth(), decoder.getHeight());

            this.scaler = Math.min(this.imageSize.x, this.imageSize.y);
            this.scale = new Vector2f(this.scaler, this.scaler);

            if(this.imageSize.x != this.imageSize.y) {
                this.totalFrame = Math.max(this.imageSize.x, this.imageSize.y) / (int) scaler;
            }

            ByteBuffer buffer = BufferUtils.createByteBuffer(this.imageSize.x * this.imageSize.y * COLOR_SIZE);
            decoder.decode(buffer, this.imageSize.x * COLOR_SIZE, PNGDecoder.Format.RGBA);
            buffer.flip();

            this.id = GL40.glGenTextures();
            this.bind();

            GL40.glTexParameteri(GL40.GL_TEXTURE_RECTANGLE, GL40.GL_TEXTURE_WRAP_S, GL40.GL_CLAMP_TO_EDGE);
            GL40.glTexParameteri(GL40.GL_TEXTURE_RECTANGLE, GL40.GL_TEXTURE_WRAP_T, GL40.GL_CLAMP_TO_EDGE);

            GL40.glTexParameteri(GL40.GL_TEXTURE_RECTANGLE, GL40.GL_TEXTURE_MIN_FILTER, interpolation.value());
            GL40.glTexParameteri(GL40.GL_TEXTURE_RECTANGLE, GL40.GL_TEXTURE_MAG_FILTER, interpolation.value());

            GL40.glTexImage2D(GL40.GL_TEXTURE_RECTANGLE, 0, GL40.GL_RGBA, this.imageSize.x, this.imageSize.y, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, buffer);
            this.unbind();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Texture(@NonNull String name) {
        this(name, TextureInterpolation.NEAREST);
    }

    public Texture(TextureAtlas atlas, String name, Vector2f position, Vector2i size) {
        this.name = name;
        this.id = atlas.texture().id;
        this.rootPosition = position;
        this.position = new Vector2f(0f, 0f);
        this.imageSize = size;

        this.scaler = Math.min(this.imageSize.x, this.imageSize.y);
        this.scale = new Vector2f(this.scaler, this.scaler);

        if(this.imageSize.x != this.imageSize.y) {
            this.totalFrame = Math.max(this.imageSize.x, this.imageSize.y) / (int) this.scaler;
        }
    }

    public Texture(TextureAtlas atlas, String name, Vector2f position, int size) {
        this(atlas, name, position, new Vector2i(size, size));
    }

    @Override
    public void bind() {
        if(this.id != LAST_BIND) {
            LAST_BIND = this.id;
            GL40.glBindTexture(GL40.GL_TEXTURE_RECTANGLE, this.id);
        }
    }

    @Override
    public void unbind() {
        LAST_BIND = 0;
        GL40.glBindTexture(GL40.GL_TEXTURE_RECTANGLE, 0);
    }

    @Override
    public void dispose() {
        GL40.glDeleteTextures(this.id);
    }

    public Texture animated(boolean animated) {
        if(this.animated != animated)
            this.returning = !animated;
        this.animated = animated;
        return this;
    }

    @Override
    public Vector3f rotation() {
        return new Vector3f(0, 0, 0);
    }

    @Override
    public Matrix4f texCoordTransformation() {
        if(this.animated || this.returning) {
            if (this.changed == 0) this.changed = Clock.monotonic();
            if(this.returning) {
                if (this.changed + RETURN_FRAME_TIME <= Clock.monotonic()) {
                    this.frame += (this.defaultFrame >= this.frame ? 1 : -1);
                    if (this.frame == this.defaultFrame) this.returning = false;
                    this.changed = Clock.monotonic();
                }
            } else {
                if (this.changed + FRAME_TIME <= Clock.monotonic()) {
                    this.frame += 1;
                    if (this.frame == this.totalFrame) this.frame = 0;
                    this.changed = Clock.monotonic();
                }
            }
        }

        if (this.imageSize.x > this.imageSize.y) {
            this.position.x = this.scale.x * this.frame;
        } else if (this.imageSize.y > this.imageSize.x) {
            this.position.y = this.scale.y * this.frame;
        }

        this.position.add(this.rootPosition);
        Matrix4f transformation = this.transformation();
        this.position.sub(this.rootPosition);

        return transformation;
    }
}
