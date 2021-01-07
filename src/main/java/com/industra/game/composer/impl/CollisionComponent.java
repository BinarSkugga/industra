/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.composer.impl;

import com.industra.engine.Collidable;
import com.industra.engine.physic.CollisionBox;
import com.industra.engine.physic.World;
import com.industra.game.composer.Component;
import com.industra.game.composer.Entity2D;
import lombok.Getter;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class CollisionComponent extends Component implements Collidable {
    @Getter private CollisionBox box;

    public CollisionComponent(Entity2D parent, CollisionBox box, Vector2f position, float angle) {
        super(parent);
        this.box = box;

        Body body = World.get().b2dworld().createBody(this.box.bodyDef());
        this.box.spawn(body, position, angle);
        this.parent.transform(box);

        World.get().register(this);
    }

    public CollisionComponent(Entity2D parent, CollisionBox box, Vector2f position) {
        this(parent, box, position, 0f);
    }

    public CollisionComponent(Entity2D parent, CollisionBox box) {
        this(parent, box, new Vector2f(0f, 0f), 0f);
    }

    @Override
    public void update() {
        this.parent.transform(this.box);
    }
}
