/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public interface SimplifiedTransformable extends Transformable {
    Vector2f position();
    float scaleXY();
    float rotationZ();

    @Override
    default Vector2f scale() {
        return new Vector2f(this.scaleXY(), this.scaleXY());
    }

    @Override
    default Vector3f rotation() {
        return new Vector3f(0, 0, this.rotationZ());
    }

    default Matrix4f transformation() {
        Matrix4f transformationMatrix = new Matrix4f().identity();
        transformationMatrix.translate(this.position().x, this.position().y, 0);
        transformationMatrix.rotate((float) Math.toRadians(this.rotationZ()), new Vector3f(0, 0, 1));
        transformationMatrix.scaleXY(this.scaleXY(), this.scaleXY());

        return transformationMatrix;
    }
}
