/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.composer;

import com.industra.engine.ResourceManager;
import lombok.Getter;

public abstract class Component {
    @Getter protected Entity2D parent;

    public Component(Entity2D parent) {
        this.parent = parent;
    }

    public void register(ResourceManager manager) {}
}
