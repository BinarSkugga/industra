/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game.shaders;

import com.industra.Constants;
import com.industra.engine.graphic.shader.ShaderProgram;
import org.joml.Matrix4f;

public class BaseShader extends ShaderProgram<BaseShaderable> {
    private Matrix4f ortho;

    public BaseShader() {
        super("base");
        this.ortho = new Matrix4f().setOrtho2D(0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 0);
    }

    @Override
    public void accept(BaseShaderable object) {
        this.load("projection", this.ortho);
        this.load("transformation", object.transformation());
        this.load("texCoordTransformation", object.texture().texCoordTransformation());
    }

    @Override
    protected void registerAttributes() {
        this.attribute("position", 0);
        this.attribute("texCoord", 1);
    }

    @Override
    protected void registerUniforms() {
        this.uniform("texCoordTransformation");
        this.uniform("projection");
        this.uniform("transformation");
    }
}
