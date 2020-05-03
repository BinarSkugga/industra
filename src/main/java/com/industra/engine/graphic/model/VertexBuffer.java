/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.model;

import com.industra.engine.Bindable;
import com.industra.engine.Disposable;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL40;

public abstract class VertexBuffer implements Disposable, Bindable {
    @Getter protected int id;
    @Getter @Setter private int target = GL40.GL_ARRAY_BUFFER;
    @Getter @Setter private int usage = GL40.GL_STATIC_DRAW;

    public VertexBuffer() {
        this.id = GL40.glGenBuffers();
    }

    @Override
    public void bind() {
        GL40.glBindBuffer(this.target, this.id);
    }

    @Override
    public void unbind() {
        GL40.glBindBuffer(this.target, 0);
    }

    @Override
    public void dispose() {
        GL40.glDeleteBuffers(this.id);
    }
}
