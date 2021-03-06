/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game.shaders;

import com.industra.Constants;
import com.industra.engine.graphic.shader.ShaderProgram;
import com.industra.game.composer.impl.TexturedModelComponent;
import org.joml.Matrix4f;

public class BaseShader extends ShaderProgram<TexturedModelComponent> {
    private Matrix4f ortho;

    public interface ShaderInterface {
        Matrix4f transformation();
        Matrix4f textureTransformation();
    }

    public BaseShader() {
        super("base");
        this.ortho = new Matrix4f().setOrtho2D(0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 0);
    }

    @Override
    public void accept(TexturedModelComponent object) {
        this.load("projection", this.ortho);
        this.load("transformation", object.transformation());
        this.load("texCoordTransformation", object.textureTransformation());
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
