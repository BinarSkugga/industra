/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.texture;

import lombok.Getter;
import org.lwjgl.opengl.GL40;

public enum TextureInterpolation {
    NEAREST(GL40.GL_NEAREST),
    LINEAR(GL40.GL_LINEAR);

    @Getter private int value;

    TextureInterpolation(int value) {
        this.value = value;
    }
}
