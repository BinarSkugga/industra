/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import lombok.Getter;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;


public class VertexArray implements Disposable {
    private int id;
    @Getter private VertexBuffer indices;
    @Getter private List<VertexBuffer> buffers = new ArrayList<>();

    public VertexArray() {
        this.id = GL30.glGenVertexArrays();
    }

    public void addFloat(FloatVertexBuffer vbo, int size) {
        vbo.bind();
        GL15.glBufferData(vbo.target(), vbo.data(), vbo.usage());
        GL20.glVertexAttribPointer(buffers.size(), size, vbo.type(), false, 0, 0);
        vbo.unbind();

        buffers.add(vbo);
    }

    public void addFloat(FloatVertexBuffer vbo) {
        this.addFloat(vbo, 2);
    }

    public void addInt(IntVertexBuffer vbo, int size) {
        vbo.bind();
        GL15.glBufferData(vbo.target(), vbo.data(), vbo.usage());
        GL20.glVertexAttribPointer(buffers.size(), size, vbo.type(), false, 0, 0);
        vbo.unbind();

        buffers.add(vbo);
    }

    public void addInt(IntVertexBuffer vbo) {
        this.addInt(vbo, 2);
    }

    public void addIndices(IntVertexBuffer vbo) {
        vbo.target(GL15.GL_ELEMENT_ARRAY_BUFFER);

        vbo.bind();
        GL15.glBufferData(vbo.target(), vbo.data(), vbo.usage());
        this.indices = vbo;
    }

    public void bind() {
        GL30.glBindVertexArray(this.id);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
        this.indices.unbind();
    }

    @Override
    public void dispose() {
        for (VertexBuffer vbo : this.buffers) vbo.dispose();
        this.indices.dispose();
        GL30.glDeleteVertexArrays(this.id);
    }
}
