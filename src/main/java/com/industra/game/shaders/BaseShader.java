/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game.shaders;

import com.industra.Constants;
import com.industra.engine.graphic.shader.ShaderProgram;
import com.industra.engine.graphic.texture.SubTexture;
import com.industra.game.PositionedModel;
import com.industra.utils.Logger;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class BaseShader extends ShaderProgram<PositionedModel> {
    private Matrix4f ortho;

    public BaseShader() {
        super("base");
        this.ortho = new Matrix4f().setOrtho2D(0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 0);
    }

    @Override
    public void accept(PositionedModel object) {
        if(SubTexture.class.isAssignableFrom(object.model().texture().getClass())){
            Vector4f position = new Vector4f(((SubTexture)object.model().texture()).position().x, ((SubTexture)object.model().texture()).position().y, 1, 1);
            Logger.out(position.mul(object.model().texture().texCoordTransformation()));
        }
        this.load("texCoordTransformation", object.model().texture().texCoordTransformation());
        this.load("projection", this.ortho);
        this.load("transformation", object.transformation());
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
