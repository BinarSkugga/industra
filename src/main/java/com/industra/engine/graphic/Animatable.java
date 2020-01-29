/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.utils.Clock;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public interface Animatable extends Transformable {
    boolean animated();
    Animatable animated(boolean animated);
    long lastFrameTime();
    Animatable lastFrameTime(long time);
    int frame();
    Animatable frame(int frame);
    Vector2f size();

    default int totalFrame() {
        if(this.size().x > this.size().y) {
            return (int) (this.size().x / this.size().y);
        } else if(this.size().x < this.size().y) {
            return (int) (this.size().y / this.size().x);
        } else {
            return 1;
        }
    }

    default Matrix4f frameTransformation(long delta) {
        if(this.lastFrameTime() == 0) {
            this.lastFrameTime(Clock.monotonic());
            float scale = Math.min(this.scale().x, this.scale().y);
            this.scale().x = scale;
            this.scale().y = scale;
        }
        if (Clock.monotonic() - this.lastFrameTime() >= delta) {
            this.frame(this.frame() + 1);
            if (this.frame() == this.totalFrame())
                this.frame(0);
            this.lastFrameTime(Clock.monotonic());
        }

        Matrix4f transformation = new Matrix4f().identity();
        transformation.translate(this.scale().x * this.frame(), 0, 0);
        return transformation;
    }

}
