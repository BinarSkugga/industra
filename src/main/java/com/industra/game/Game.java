/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.Constants;
import com.industra.engine.Disposable;
import com.industra.engine.Window;
import com.industra.engine.graphic.GLContext;

public class Game implements Disposable {
    private Window window;

    public void run() {
        this.init();
        this.window = new Window(1280, 720, Constants.GAME_TITLE);
        this.window.context(new GLContext(this.window, 60));
        this.window.init();

        this.window.run();
        this.dispose();
    }

    public void init() {
        FakePlayer player = new FakePlayer();
    }

    @Override
    public void dispose() {
        this.window.dispose();
    }
}
