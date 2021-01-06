/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.physic.materials;

import com.industra.engine.graphic.Material;
import org.jbox2d.dynamics.BodyType;

public class BaseMaterial extends Material {
    public BaseMaterial() {
        this.density = 1f;
        this.friction = 1f;
        this.type = BodyType.DYNAMIC;
    }
}
