/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.model;

import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

import java.nio.FloatBuffer;

public class FloatVertexBuffer extends VertexBuffer<float[]> {
    @Getter private FloatBuffer bufferData;
    @Getter private float[] data;

    @Getter private int type = GL40.GL_FLOAT;

    public FloatVertexBuffer(@NonNull float[] data) {
        super();
        this.fill(data);
    }

    public void fill(@NonNull float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        this.data = data;
        this.bufferData = buffer;
    }
}
