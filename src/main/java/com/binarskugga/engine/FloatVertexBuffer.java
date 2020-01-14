/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class FloatVertexBuffer extends VertexBuffer {
    @Getter private FloatBuffer data;
    @Getter private int type = GL11.GL_FLOAT;

    public FloatVertexBuffer(@NonNull float[] data) {
        super();
        this.fill(data);
    }

    public void fill(@NonNull float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        this.data = buffer;
    }
}
