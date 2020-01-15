/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.Bindable;
import com.industra.engine.Disposable;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram implements Disposable, Bindable {
    private int id;
    private VertexShader vShader;
    private FragmentShader fShader;

    public ShaderProgram(VertexShader vShader, FragmentShader fShader) {
        this.vShader = vShader;
        this.fShader = fShader;
        this.id = GL20.glCreateProgram();

        GL20.glAttachShader(this.id, this.vShader.id());
        GL20.glAttachShader(this.id, this.fShader.id());
        GL20.glLinkProgram(this.id);
        GL20.glValidateProgram(this.id);
    }

    public ShaderProgram(String name) {
        this(new VertexShader(name), new FragmentShader(name));
    }

    @Override
    public void bind() {
        GL20.glUseProgram(this.id);
    }

    @Override
    public void unbind() {
        GL20.glUseProgram(0);
    }

    @Override
    public void dispose() {
        this.unbind();
        GL20.glDetachShader(this.id, this.vShader.id());
        GL20.glDetachShader(this.id, this.fShader.id());
        GL20.glDeleteShader(this.vShader.id());
        GL20.glDeleteShader(this.fShader.id());
        GL20.glDeleteProgram(this.id);
    }
}
