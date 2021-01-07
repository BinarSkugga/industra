/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine;

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
    Vector2f animationSize();

    default int totalFrame() {
        if(this.animationSize().x > this.animationSize().y) {
            return (int) (this.animationSize().x / this.animationSize().y);
        } else if(this.animationSize().x < this.animationSize().y) {
            return (int) (this.animationSize().y / this.animationSize().x);
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
        else if (Clock.monotonic() - this.lastFrameTime() >= delta) {
            this.frame(this.frame() + 1);
            if (this.frame() == this.totalFrame())
                this.frame(0);
            this.lastFrameTime(Clock.monotonic());
        }

        Matrix4f transformation = new Matrix4f().identity();
        transformation.translate(this.position().x + (this.frame() * this.scale().x), this.position().y, 0);
        return transformation;
    }

}
