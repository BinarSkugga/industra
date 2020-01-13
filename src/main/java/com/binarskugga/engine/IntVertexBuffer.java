/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.IntBuffer;

public class IntVertexBuffer extends VertexBuffer {
    @Getter private IntBuffer data;
    @Getter private int type = GL11.GL_INT;

    public IntVertexBuffer(int[] data) {
        super();
        this.fill(data);
    }

    public void fill(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        this.data = buffer;
    }
}
