/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class SubTexture implements Texturable, SimplifiedTransformable {
    @Getter private TextureAtlas atlas;
    @Getter private Vector2f position;
    @Getter private float scaleXY;

    public SubTexture(TextureAtlas atlas, Vector2f position, float size) {
        this.atlas = atlas;
        this.position = position;
        this.scaleXY = size;
    }

    @Override
    public float rotationZ() {
        return 0;
    }

    @Override
    public void bind() {
        this.atlas.bind();
    }

    @Override
    public void unbind() {
        this.atlas.unbind();
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
    }

    @Override
    public Matrix4f texCoordTransformation() {
        return this.transformation();
    }
}
