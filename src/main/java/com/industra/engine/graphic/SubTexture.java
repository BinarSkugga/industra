/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SubTexture implements Texturable, Animatable, Transformable {
    @Getter private TextureAtlas atlas;
    @Getter private Vector2f position;
    @Getter private Vector2f scale;
    @Getter private Vector2f size;

    @Getter @Setter private boolean animated = false;
    @Getter @Setter private long lastFrameTime = 0;
    @Getter @Setter private int frame = 0;

    public SubTexture(TextureAtlas atlas, Vector2f position, Vector2f size) {
        this.atlas = atlas;
        this.position = position;
        this.scale = size;
        this.size = new Vector2f(size);
    }

    public SubTexture(TextureAtlas atlas, Vector2f position, float size) {
        this(atlas, position, new Vector2f(size, size));
    }

    @Override
    public Vector3f rotation() {
        return new Vector3f(0, 0, 0);
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
        if(this.animated) {
            return this.frameTransformation(50).mul(this.transformation());
        } else {
            return this.transformation();
        }
    }
}
