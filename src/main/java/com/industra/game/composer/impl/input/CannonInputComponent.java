/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.composer.impl.input;

import com.industra.engine.input.InputList;
import com.industra.engine.input.Key;
import com.industra.game.Cannon;
import com.industra.game.composer.AbstractInputComponent;
import com.industra.game.composer.impl.CollisionComponent;

public class CannonInputComponent extends AbstractInputComponent<Cannon> {
    private CollisionComponent cc;

    public CannonInputComponent(Cannon parent) {
        super(parent);
        this.cc = parent.getComponent(CollisionComponent.class);
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
        if(held.has(Key.Q))
            this.cc.box().torque(-1f);
        if(held.has(Key.E))
            this.cc.box().torque(1f);
    }
}
