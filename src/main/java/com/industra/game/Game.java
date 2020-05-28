/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.ResourceManager;
import com.industra.engine.Window;
import com.industra.engine.graphic.GLContext;
import com.industra.engine.graphic.Material;
import com.industra.engine.graphic.model.Model;
import com.industra.engine.graphic.physics.CollisionBox;
import com.industra.engine.graphic.physics.materials.CharacterMaterial;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.graphic.texture.TextureAtlas;
import com.industra.game.shaders.BaseShader;
import org.joml.Vector2f;

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
        rm.register(new Texture("fighter").multiLine(new Vector2f(32, 48)));

        // Material
        Material defMat = new CharacterMaterial();

        // Shaders & Models
        BaseShader baseShader = new BaseShader();
        baseShader.addEntity(new PhysicalModel(
                        Model.load("square"),
                        new CollisionBox(new Vector2f(30f), new Vector2f(100f, 100f), defMat),
                        rm.getTexture("fighter")
                ));

        rm.register(baseShader);
    }

    @Override
    public void dispose() {
        ResourceManager.get().dispose();
        Window.get().dispose();

        System.exit(0);
    }
}
