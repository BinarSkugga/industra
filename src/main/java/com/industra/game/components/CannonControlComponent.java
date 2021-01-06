/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.components;

import com.industra.engine.Entity;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;

public class CannonControlComponent extends Component implements InputListener {
    private final CollisionComponent collisionComponent;

    public CannonControlComponent(Entity parent, CollisionComponent collisionComponent) {
        super(parent);
        InputTracker.get().subscribe(this);

        this.collisionComponent = collisionComponent;
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
        if(held.has(Key.Q))
            this.collisionComponent.box().torque(-1f);
        if(held.has(Key.E))
            this.collisionComponent.box().torque(1f);
    }
}
