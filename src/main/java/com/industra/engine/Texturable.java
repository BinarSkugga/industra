/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine;

import org.joml.Matrix4f;

public interface Texturable extends Bindable, Disposable {
    // Returns a matrix4f that transforms texture coordinates. Used in sub textures.
    default Matrix4f texCoordTransformation() {
        return new Matrix4f().identity();
    }
    Texturable animated(boolean value);
}
