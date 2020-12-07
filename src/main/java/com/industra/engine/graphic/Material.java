/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import lombok.Getter;
import lombok.Setter;
import org.jbox2d.dynamics.BodyType;

public abstract class Material {
    @Getter @Setter protected float density = 1f;
    @Getter @Setter protected float friction = 0.2f;
    @Getter @Setter protected float restitution = 0f;
    @Getter @Setter protected BodyType type = BodyType.DYNAMIC;
}
