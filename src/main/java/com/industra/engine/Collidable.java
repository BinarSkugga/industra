/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine;

import com.industra.engine.physic.CollisionBox;

public interface Collidable extends Updatable {
    CollisionBox box();
}
