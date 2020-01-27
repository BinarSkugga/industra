/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game.shaders;

import com.industra.engine.graphic.ShaderProgram;
import com.industra.game.PositionedModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BaseShader extends ShaderProgram<PositionedModel> {
    private Matrix4f ortho;

    public BaseShader() {
        super("base");
        this.ortho = new Matrix4f().setOrtho2D(0, 1280, 720, 0);
    }

    @Override
    public void accept(PositionedModel object) {
        this.load("projection", this.ortho);

        Matrix4f transformationMatrix = new Matrix4f().identity();
        transformationMatrix.translate(object.position().x, object.position().y, 0);
        transformationMatrix.rotate((float) Math.toRadians(object.rotation()), new Vector3f(0, 0, 1));
        transformationMatrix.scaleXY(60, 60);

        this.load("transformation", transformationMatrix);
    }

    @Override
    protected void registerAttributes() {
        this.attribute("position", 0);
        this.attribute("texCoords", 1);
    }

    @Override
    protected void registerUniforms() {
        this.uniform("projection");
        this.uniform("transformation");
    }
}
