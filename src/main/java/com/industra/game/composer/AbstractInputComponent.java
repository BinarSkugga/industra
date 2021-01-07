/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.composer;

import com.industra.engine.Controllable;
import com.industra.engine.input.InputTracker;

public abstract class AbstractInputComponent<T extends Entity2D> extends Component implements Controllable {
    public AbstractInputComponent(T parent) {
        super(parent);
        InputTracker.get().subscribe(this);
    }
}
