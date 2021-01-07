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
import com.industra.engine.physic.CollisionBox;
import com.industra.engine.physic.materials.BaseMaterial;
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

        // Shaders & Models
        BaseShader baseShader = new BaseShader();

        Turret turret = new Turret();
        Cannon cannon = new Cannon().turret(turret);

        baseShader.addEntities(turret, cannon);
        rm.register(baseShader);
    }

    @Override
    public void dispose() {
        ResourceManager.get().dispose();
        Window.get().dispose();

        System.exit(0);
    }
}
