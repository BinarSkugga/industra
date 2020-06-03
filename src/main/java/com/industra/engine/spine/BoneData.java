/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class BoneData {
    @Getter private int index;
    @Getter private String name;
    @Getter private BoneData parent;
    @Getter @Setter private float length;

    @Getter @Setter private float x;
    @Getter @Setter private float y;
    @Getter @Setter private float rotation;
    @Getter @Setter private float scaleX = 1;
    @Getter @Setter private float scaleY = 1;
    @Getter @Setter private float shearX;
    @Getter @Setter private float shearY;

    @Getter @Setter private TransformMode transformMode = TransformMode.NORMAL;
    @Getter @Setter private boolean skinRequired;

    public BoneData(int index, @NonNull String name, BoneData data) {
        if (index < 0) throw new RuntimeException("index must be >= 0");

        this.index = index;
        this.name = name;
        this.parent = data;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
