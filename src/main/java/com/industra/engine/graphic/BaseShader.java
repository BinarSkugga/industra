/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.game.PositionedModel;
import org.joml.Matrix4f;

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
        this.load("transformation", transformationMatrix);
    }

    @Override
    protected void registerAttributes() {
        this.attribute("position", 0);
    }

    @Override
    protected void registerUniforms() {
        this.uniform("projection");
        this.uniform("transformation");
    }
}
