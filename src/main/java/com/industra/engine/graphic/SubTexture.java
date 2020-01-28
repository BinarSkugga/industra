/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SubTexture implements Texturable, Transformable {
    @Getter private TextureAtlas atlas;
    @Getter private Vector2f position;
    @Getter private Vector2f scale;

    @Getter private boolean animated = false;
    private int delta;
    private int lastFrameTime = 0;
    private int totalFrame = 0;
    private int currentFrame = 0;

    public SubTexture(TextureAtlas atlas, Vector2f position, Vector2f size) {
        this.atlas = atlas;
        this.position = position;
        this.scale = size;
    }

    public SubTexture(TextureAtlas atlas, Vector2f position, float size) {
        this(atlas, position, new Vector2f(size, size));
    }

    private int time() {
        return (int) (System.nanoTime() / 1000000);
    }

    public void animate(int delta) {
        this.animated = true;
        this.delta = delta;

        if(this.scale.x > this.scale.y) {
            this.totalFrame = (int) Math.floor(this.scale.x / this.scale.y);
        } else if(this.scale.x < this.scale.y) {
            this.totalFrame = (int) Math.floor(this.scale.y / this.scale.x);
        } else {
            this.totalFrame = 1;
        }

        float frameSize = Math.min(this.scale.x, this.scale.y);
        this.scale = new Vector2f(frameSize, frameSize);
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
            if(this.lastFrameTime == 0) this.lastFrameTime = this.time();
            if (time() - this.lastFrameTime >= this.delta) {
                this.currentFrame += 1;
                if (this.currentFrame == this.totalFrame)
                    this.currentFrame = 0;
                this.lastFrameTime = this.time();
            }

            Matrix4f frameTransformation = new Matrix4f().identity();
            frameTransformation.translate(this.scale.x * this.currentFrame, 0, 0);
            return frameTransformation.mul(this.transformation());
        } else {
            return this.transformation();
        }
    }
}
