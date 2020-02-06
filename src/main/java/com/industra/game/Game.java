/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.ResourceManager;
import com.industra.engine.Window;
import com.industra.engine.graphic.GLContext;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.graphic.texture.TextureAtlas;
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
        rm.register(new TextureAtlas("main"));
        rm.register(new Texture("animatedTexture").animated(true));

        // Shaders & Models
        BaseShader baseShader = new BaseShader();
        baseShader.addEntity(new PositionedModel("square", rm.getSubTexture("main/animated")));

        rm.register(baseShader);
    }

    @Override
    public void dispose() {
        ResourceManager.get().dispose();
        Window.get().dispose();

        System.exit(0);
    }
}
