/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game.shaders;

import com.industra.engine.graphic.ShaderProgram;
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
        this.load("transformation", object.transformation());
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
