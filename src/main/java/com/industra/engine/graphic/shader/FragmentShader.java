/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.shader;

import org.lwjgl.opengl.GL40;


public class FragmentShader extends Shader {
    public FragmentShader(String name) {
        super(name, GL40.GL_FRAGMENT_SHADER);
    }
}
