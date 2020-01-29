/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.Bindable;
import com.industra.engine.Disposable;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public interface Texturable extends Bindable, Disposable {
    // Returns a matrix4f that transforms texture coordinates. Used in sub textures.
    default Matrix4f texCoordTransformation() {
        return new Matrix4f().identity();
    }
    Vector2f size();
}
