/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.shader;


import org.lwjgl.opengl.GL20;

public class VertexShader extends Shader {
    public VertexShader(String name) {
        super(name, GL20.GL_VERTEX_SHADER);
    }
}
