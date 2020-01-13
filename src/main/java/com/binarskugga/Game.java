/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga;

import com.binarskugga.engine.GLContext;
import com.binarskugga.engine.Window;

public class Game {
    private Window window;

    public static void main(String[] args) {
        Game game = new Game();
        game.init();
        game.run();
        game.dispose();
    }

    public void init() {
        this.window = new Window(1280, 720, Constants.GAME_TITLE);
        this.window.context(new GLContext(this.window, 60));
        this.window.init();
    }

    public void run() {
        this.window.run();
    }

    public void dispose() {
        this.window.dispose();
    }
}
