/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public interface Transformable2D extends Transformable {
    Vector2f positionXY();

    // Scaling of the model for both the x and y axis
    Vector2f scaleXY();

    // Rotation of the model in degrees on the Z axis
    float rotationZ();

    @Override
    default Vector3f position() {
        return new Vector3f(this.positionXY(), 0);
    }

    @Override
    default Vector3f scale() {
        return new Vector3f(this.scaleXY(), 0);
    }

    @Override
    default Vector3f rotation() {
        return new Vector3f(0, 0, this.rotationZ());
    }

    default Matrix4f transformation() {
        Matrix4f transformationMatrix = new Matrix4f().identity();
        transformationMatrix.translate(this.position().x, this.position().y, 0);
        transformationMatrix.rotate((float) Math.toRadians(this.rotationZ()), new Vector3f(0, 0, 1));
        transformationMatrix.scaleXY(this.scaleXY().x, this.scaleXY().y);

        return transformationMatrix;
    }

    void transform(Transformable2D transformer);
}
