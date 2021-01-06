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
import com.industra.engine.graphic.physics.materials.BaseMaterial;
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
        rm.register(new Texture("turret_001").multiLine(new Vector2f(49)));
        rm.register(new Texture("cannon_001").animated(false));

        // Material
        Material defMat = new BaseMaterial();

        // Shaders & Models
        BaseShader baseShader = new BaseShader();

        Cannon cannon = new Cannon(
                Model.load("square"),
                rm.getTexture("cannon_001"),
                new CollisionBox(new Vector2f(60f), new Vector2f(-0.5f, -0.5f), defMat)
        );
        Turret turret = new Turret(
                Model.load("square"),
                rm.getTexture("turret_001"),
                new CollisionBox(new Vector2f(60f), new Vector2f(150f, 150f), defMat)
        );
        cannon.turret(turret);
        baseShader.addEntities(cannon);

        rm.register(baseShader);
    }

    @Override
    public void dispose() {
        ResourceManager.get().dispose();
        Window.get().dispose();

        System.exit(0);
    }
}
