/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.physic.CollisionBox;
import com.industra.engine.physic.World;

public interface Updatable {
    void update(World world);
    CollisionBox collisionBox();
}
