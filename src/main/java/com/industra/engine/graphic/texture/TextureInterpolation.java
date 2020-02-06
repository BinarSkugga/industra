/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.texture;

import lombok.Getter;
import org.lwjgl.opengl.GL31;

public enum TextureInterpolation {
    NEAREST(GL31.GL_NEAREST),
    LINEAR(GL31.GL_LINEAR);

    @Getter private int value;

    TextureInterpolation(int value) {
        this.value = value;
    }
}
