/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.Setter;

public class PowerInterpolation extends Interpolation {
    @Getter @Setter private float power;

    public PowerInterpolation(float power) {
        this.power = power;
    }

    @Override
    protected float apply(float a) {
        if (a <= 0.5f) return (float)Math.pow(a * 2, this.power) / 2;
        return (float)Math.pow((a - 1) * 2, this.power) / (this.power % 2 == 0 ? -2 : 2) + 1;
    }
}
