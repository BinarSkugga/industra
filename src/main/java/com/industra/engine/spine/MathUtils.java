/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import java.util.Random;

public class MathUtils {
    public static final float PI = 3.1415927f;
    public static final float PI2 = PI * 2;
    public static final float RAD_DEG = 180f / PI;
    public static final float DEG_RAD = PI / 180;

    private static Random random = new Random();

    static public float sin (float radians) {
        return (float)Math.sin(radians);
    }

    static public float cos (float radians) {
        return (float)Math.cos(radians);
    }

    static public float sinDeg(float degrees) {
        return (float)Math.sin(degrees * DEG_RAD);
    }

    static public float cosDeg(float degrees) {
        return (float)Math.cos(degrees * DEG_RAD);
    }

    static public float atan2 (float y, float x) {
        return (float)Math.atan2(y, x);
    }

    static public float clamp (float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    static public float randomTriangle(float min, float max) {
        return randomTriangle(min, max, (min + max) * 0.5f);
    }

    static public float randomTriangle(float min, float max, float mode) {
        float u = (float) random.nextDouble();
        float d = max - min;
        if (u <= (mode - min) / d) return min + (float)Math.sqrt(u * d * (mode - min));
        return max - (float)Math.sqrt((1 - u) * d * (max - mode));
    }
}
