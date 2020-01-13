/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;


public class VertexArray {
    private int id;
    @Getter private VertexBuffer indices;
    @Getter private List<VertexBuffer> buffers = new ArrayList<>();

    public VertexArray() {
        this.id = GL30.glGenVertexArrays();
    }

    public void addVertices(FloatVertexBuffer vbo) {
        vbo.bind();
        GL15.glBufferData(vbo.target(), vbo.data(), vbo.usage());
        GL20.glVertexAttribPointer(buffers.size(), 2, vbo.type(), false, 0, 0);
        vbo.unbind();

        buffers.add(vbo);
    }

    public void addIndices(IntVertexBuffer vbo) {
        vbo.bind();
        GL15.glBufferData(vbo.target(), vbo.data(), vbo.usage());
        this.indices = vbo;
    }

    public void bind() {
        GL30.glBindVertexArray(this.id);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void dispose() {
        for(VertexBuffer vbo : this.buffers) vbo.dispose();
        this.indices.dispose();
        GL30.glDeleteVertexArrays(this.id);
    }
}
