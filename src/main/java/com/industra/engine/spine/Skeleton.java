/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Skeleton {
    @Getter @Setter private List<Bone> bones;

    @Getter @Setter private float scaleX = 1;
    @Getter @Setter private float scaleY = 1;
    @Getter @Setter private float x;
    @Getter @Setter private float y;
}
