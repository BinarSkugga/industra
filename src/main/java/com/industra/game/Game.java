/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.ResourceManager;
import com.industra.engine.Window;
import com.industra.engine.graphic.Texture;
import com.industra.game.shaders.BaseShader;
import com.industra.engine.graphic.GLContext;

public class Game implements Disposable {
    public void run() {
        Window window = Window.get();
        window.context(new GLContext());
        window.init();

        this.init();
        window.run();
        this.dispose();
    }

    public void init() {
        ResourceManager rm = ResourceManager.get();

        // Textures
        rm.register(
                new Texture("default"),
                new Texture("sprite")
        );

        // Shaders & Models
        BaseShader baseShader = new BaseShader();
        baseShader.addEntity(new PositionedModel("square"));

        rm.register(baseShader);
    }

    @Override
    public void dispose() {
        Window.get().dispose();
    }
}
