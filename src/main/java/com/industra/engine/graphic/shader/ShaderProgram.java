/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.shader;

import com.industra.engine.Bindable;
import com.industra.engine.Disposable;
import com.industra.engine.graphic.Drawable;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

import java.nio.FloatBuffer;
import java.util.*;
import java.util.function.Consumer;

public abstract class ShaderProgram<T extends Drawable> implements Disposable, Bindable, Consumer<T> {
    private static int LAST_BIND = 0;

    private int id;
    private VertexShader vShader;
    private FragmentShader fShader;

    private Map<String, Integer> locations;
    private List<T> entities;

    public ShaderProgram(VertexShader vShader, FragmentShader fShader) {
        this.vShader = vShader;
        this.fShader = fShader;
        this.id = GL40.glCreateProgram();
        this.locations = new HashMap<>();
        this.entities = new ArrayList<>();

        GL40.glAttachShader(this.id, this.vShader.id());
        GL40.glAttachShader(this.id, this.fShader.id());
        this.registerAttributes();

        GL40.glLinkProgram(this.id);
        GL40.glValidateProgram(this.id);
        this.registerUniforms();
    }

    public ShaderProgram(String name) {
        this(new VertexShader(name), new FragmentShader(name));
    }

    protected abstract void registerAttributes();

    protected abstract void registerUniforms();

    protected void attribute(String name, int index) {
        GL40.glBindAttribLocation(this.id, index, name);
    }

    protected void uniform(String name) {
        int location = GL40.glGetUniformLocation(this.id, name);
        this.locations.put(name, location);
    }

    protected void load(String name, Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);

        GL40.glUniformMatrix4fv(this.locations.get(name), false, buffer);
    }

    @Override
    public void bind() {
        if(this.id != LAST_BIND) {
            LAST_BIND = this.id;
            GL40.glUseProgram(this.id);
        }
    }

    @Override
    public void unbind() {
        GL40.glUseProgram(0);
        LAST_BIND = 0;
    }

    @Override
    public void dispose() {
        for (T entity : this.entities) {
            if (Disposable.class.isAssignableFrom(entity.getClass()))
                ((Disposable) entity).dispose();
        }

        this.unbind();
        GL40.glDetachShader(this.id, this.vShader.id());
        GL40.glDetachShader(this.id, this.fShader.id());
        GL40.glDeleteShader(this.vShader.id());
        GL40.glDeleteShader(this.fShader.id());
        GL40.glDeleteProgram(this.id);
    }

    public T addEntity(T entity) {
        this.entities.add(entity);
        return entity;
    }

    @SafeVarargs
    public final List<T> addEntities(T... entities) {
        List<T> ents = Arrays.asList(entities);
        this.entities.addAll(ents);
        return ents;
    }

    public void render() {
        this.bind();
        this.entities.forEach(entity -> {
            this.accept(entity);
            entity.draw();
        });
    }

    @Override
    public abstract void accept(T object);
}
