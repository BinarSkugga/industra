/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import com.industra.utils.Logger;

public class FakePlayer implements InputListener {
    public FakePlayer() {
        InputTracker.get().subscribe(this);
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList held, InputList released, InputList idle) {
        if(held.has(Key.W))
            Logger.out("Move up");
        if(held.has(Key.S))
            Logger.out("Move down");
        if(held.has(Key.A))
            Logger.out("Move left");
        if(held.has(Key.D))
            Logger.out("Move right");
    }
}
