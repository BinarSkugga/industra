/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import org.lwjgl.opengl.GL20;


public class FragmentShader extends Shader {
    public FragmentShader(String name) {
        super(name, GL20.GL_FRAGMENT_SHADER);
    }
}
