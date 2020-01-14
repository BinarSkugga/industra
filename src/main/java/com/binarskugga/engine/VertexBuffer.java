/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL15;

public abstract class VertexBuffer implements Disposable {
    @Getter protected int id;
    @Getter @Setter private int target = GL15.GL_ARRAY_BUFFER;
    @Getter @Setter private int usage = GL15.GL_STATIC_DRAW;

    public VertexBuffer() {
        this.id = GL15.glGenBuffers();
    }

    public void bind() {
        GL15.glBindBuffer(this.target, this.id);
    }

    public void unbind() {
        GL15.glBindBuffer(this.target, 0);
    }

    @Override
    public void dispose() {
        GL15.glDeleteBuffers(this.id);
    }
}
