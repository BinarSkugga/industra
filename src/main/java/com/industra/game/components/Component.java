/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.components;

import com.industra.engine.Disposable;
import com.industra.engine.Entity;
import com.industra.engine.graphic.Drawable;
import com.industra.engine.graphic.Updatable;
import lombok.Getter;

public abstract class Component implements Disposable, Updatable, Drawable {
    @Getter protected Entity parent;

    public Component(Entity parent) {
        this.parent = parent;
    }
}
