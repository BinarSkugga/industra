/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.shader;

import com.industra.engine.Bindable;
import com.industra.engine.Disposable;
import com.industra.engine.Drawable;
import com.industra.game.composer.Component;
import com.industra.game.composer.Entity2D;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

import java.lang.reflect.ParameterizedType;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class ShaderProgram<T extends Component & Drawable> implements Disposable, Bindable, Consumer<T> {
    private static int LAST_BIND = 0;

    private int id;
    private VertexShader vShader;
    private FragmentShader fShader;

    private Map<String, Integer> locations;
    private List<T> components;

    public ShaderProgram(VertexShader vShader, FragmentShader fShader) {
        this.vShader = vShader;
        this.fShader = fShader;
        this.id = GL40.glCreateProgram();
        this.locations = new HashMap<>();
        this.components = new ArrayList<>();

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
        for (T entity : this.components) {
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

    protected T addComponent(T component) {
        this.components.add(component);
        return component;
    }

    @SuppressWarnings("unchecked")
    public T addEntity(Entity2D entity) {
        Class<T> componentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return this.addComponent(entity.getComponent(componentClass));
    }

    protected final List<T> addComponents(List<T> components) {
        this.components.addAll(components);
        return components;
    }

    @SafeVarargs
    protected final List<T> addComponents(T... components) {
        return this.addComponents(Arrays.asList(components));
    }

    @SuppressWarnings("unchecked")
    public final List<T> addEntities(List<Entity2D> entities) {
        Class<T> componentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return this.addComponents(
                entities.stream()
                        .map(e -> e.getComponent(componentClass))
                        .collect(Collectors.toList())
        );
    }

    public final List<T> addEntities(Entity2D... entities) {
        return this.addEntities(Arrays.asList(entities));
    }

    public void render() {
        this.bind();
        this.components.forEach(component -> {
            this.accept(component);
            component.draw();
        });
    }

    @Override
    public abstract void accept(T object);
}
