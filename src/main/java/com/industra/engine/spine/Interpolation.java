/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

public abstract class Interpolation {
    public static Interpolation pow2 = new PowerInterpolation(2);
    public static Interpolation pow2Out = new PowerOutInterpolation(2);

    protected abstract float apply(float a);

    public float apply(float start, float end, float a) {
        return start + (end - start) * apply(a);
    }
}
