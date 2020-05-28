/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import lombok.Getter;
import lombok.Setter;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public abstract class Material {
    @Getter private static World world = com.industra.engine.graphic.physics.World.get();

    @Getter @Setter protected float density = 0f;
    @Getter @Setter protected float friction = 0.2f;
    @Getter @Setter protected float restitution = 0f;
    @Getter @Setter protected BodyType type = BodyType.DYNAMIC;
}
