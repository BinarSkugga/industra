/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.Window;
import com.industra.engine.graphic.GLContext;

public class Game implements Disposable {
    public void run() {
        this.init();
        Window window = Window.get();
        window.context(new GLContext());
        window.init();

        window.run();
        this.dispose();
    }

    public void init() {

    }

    @Override
    public void dispose() {
        Window.get().dispose();
    }
}
