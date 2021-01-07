/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.model;

import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

import java.nio.IntBuffer;

public class IntVertexBuffer extends VertexBuffer<int[]> {
    @Getter private IntBuffer bufferData;
    @Getter private int[] data;

    @Getter private int type = GL40.GL_INT;

    public IntVertexBuffer(@NonNull int[] data) {
        super();
        this.fill(data);
    }

    public void fill(@NonNull int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        this.data = data;
        this.bufferData = buffer;
    }
}
