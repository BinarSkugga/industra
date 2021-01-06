/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.graphic.physics.World;

public interface Updatable {
    default void update(World world) {}
}
