/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public interface Transformable {
    Vector3f position();
    Vector3f scale();
    Vector3f rotation();

    default Matrix4f transformation() {
        Matrix4f transformationMatrix = new Matrix4f().identity();

        transformationMatrix.translate(this.position().x, this.position().y, this.position().z);
        transformationMatrix.rotate((float) Math.toRadians(this.rotation().x), new Vector3f(1, 0, 0));
        transformationMatrix.rotate((float) Math.toRadians(this.rotation().y), new Vector3f(0, 1, 0));
        transformationMatrix.rotate((float) Math.toRadians(this.rotation().z), new Vector3f(0, 0, 1));
        transformationMatrix.scale(this.scale().x, this.scale().y, this.scale().z);

        return transformationMatrix;
    }
}
