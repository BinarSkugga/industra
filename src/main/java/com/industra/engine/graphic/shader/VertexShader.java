/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.shader;


import org.lwjgl.opengl.GL40;

public class VertexShader extends Shader {
    public VertexShader(String name) {
        super(name, GL40.GL_VERTEX_SHADER);
    }
}
