/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.graphic.physics.CollisionBox;
import com.industra.engine.graphic.physics.World;

public interface Updatable {
    void update(World world);
    CollisionBox collisionBox();
}
