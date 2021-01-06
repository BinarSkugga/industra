/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.components;

import com.industra.engine.Entity;
import com.industra.engine.graphic.physics.CollisionBox;
import lombok.Getter;

public class CollisionComponent extends Component {
    @Getter private final CollisionBox box;

    public CollisionComponent(Entity parent, CollisionBox box) {
        super(parent);
        this.box = box;
    }
}
