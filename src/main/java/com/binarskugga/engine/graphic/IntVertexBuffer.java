/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine.graphic;

import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

public class IntVertexBuffer extends VertexBuffer {
    @Getter private IntBuffer data;
    @Getter private int type = GL11.GL_INT;

    public IntVertexBuffer(@NonNull int[] data) {
        super();
        this.fill(data);
    }

    public void fill(@NonNull int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        this.data = buffer;
    }
}
