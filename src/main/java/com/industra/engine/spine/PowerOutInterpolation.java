/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

public class PowerOutInterpolation extends PowerInterpolation {
    public PowerOutInterpolation(float power) {
        super(power);
    }

    @Override
    protected float apply(float a) {
        return (float)Math.pow(a - 1, this.power()) * (this.power() % 2 == 0 ? -1 : 1) + 1;
    }
}
