/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;

public enum TransformMode {
    NORMAL(0),
    ONLY_TRANSITION(7),
    NO_ROTATION_OR_REFLECTION(1),
    NO_SCALE(2),
    NO_SCALE_OR_REFLECTION(6);

    @Getter private int value;

    TransformMode(int value) {
        this.value = value;
    }
}
