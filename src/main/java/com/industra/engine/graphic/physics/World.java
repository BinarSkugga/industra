/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.physics;

import org.jbox2d.common.Vec2;

public class World {
    private static org.jbox2d.dynamics.World b2dWorld = new org.jbox2d.dynamics.World(new Vec2());

    public static org.jbox2d.dynamics.World get() {
        return b2dWorld;
    }
}
