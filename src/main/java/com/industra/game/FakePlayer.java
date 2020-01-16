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
    private boolean running = false;

    public FakePlayer() {
        InputTracker.get().subscribe(this);
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
        if(dpressed.any(Key.W, Key.S, Key.A, Key.D))
            this.running = true;
        if(released.any(Key.W, Key.S, Key.A, Key.D))
            this.running = false;

        if(held.has(Key.W))
            Logger.out((this.running ? "Running" : "Walking") + " UP");
        if(held.has(Key.S))
            Logger.out((this.running ? "Running" : "Walking") + " DOWN");
        if(held.has(Key.A))
            Logger.out((this.running ? "Running" : "Walking") + " LEFT");
        if(held.has(Key.D))
            Logger.out((this.running ? "Running" : "Walking") + " RIGHT");
    }
}
