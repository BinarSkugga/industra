/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.ResourceManager;
import com.industra.engine.Window;
import com.industra.engine.graphic.GLContext;
import com.industra.engine.graphic.TextureAtlas;
import com.industra.game.shaders.BaseShader;

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
        TextureAtlas mainAtlas = new TextureAtlas("main");
        rm.register(mainAtlas.texturesArray());

        // Shaders & Models
        BaseShader baseShader = new BaseShader();
        baseShader.addEntity(new PositionedModel("square", "default"));

        rm.register(baseShader);
    }

    @Override
    public void dispose() {
        ResourceManager.get().dispose();
        Window.get().dispose();
    }
}
