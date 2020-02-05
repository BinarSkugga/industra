/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import lombok.Getter;
import org.lwjgl.opengl.GL11;

public enum TextureInterpolation {
    NEAREST(GL11.GL_NEAREST),
    LINEAR(GL11.GL_LINEAR);

    @Getter private int value;

    TextureInterpolation(int value) {
        this.value = value;
    }
}
