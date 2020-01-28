/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.Bindable;
import com.industra.engine.Disposable;
import org.joml.Matrix4f;

public interface Texturable extends Bindable, Disposable {
    default Matrix4f texTransformation() {
        return new Matrix4f().identity();
    }
}
