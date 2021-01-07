/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine.graphic.model;

import lombok.Getter;

public class ModelFile {
    @Getter private String name;
    @Getter private int[] indices;
    @Getter private float[] vertices;
    @Getter private float[] texCoords;

    public ModelFile(String name, int[] indices, float[] vertices, float[] texCoords) {
        this.name = name;
        this.indices = indices;
        this.vertices = vertices;
        this.texCoords = texCoords;
    }
}
