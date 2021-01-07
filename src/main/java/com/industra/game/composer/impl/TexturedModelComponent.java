/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.composer.impl;

import com.industra.engine.Drawable;
import com.industra.engine.ResourceManager;
import com.industra.engine.graphic.model.TexturedModel;
import com.industra.game.composer.Component;
import com.industra.game.composer.Entity2D;
import com.industra.game.shaders.BaseShader;
import com.industra.utils.Logger;
import lombok.Getter;
import org.joml.Matrix4f;

public class TexturedModelComponent extends Component implements BaseShader.ShaderInterface, Drawable {
    @Getter private final TexturedModel model;

    public TexturedModelComponent(Entity2D parent, TexturedModel model) {
        super(parent);
        this.model = model;
    }

    @Override
    public void draw() {
        this.model.draw();
    }

    public void register(ResourceManager manager) {
        Logger.out("Registered model '" + this.model.name() + "' and texture '" + this.model.texture().name() + "'");
        manager.register(this.model);
    }

    @Override
    public Matrix4f transformation() {
        Logger.out(this.parent.rotationZ());
        return this.parent.transformation();
    }

    @Override
    public Matrix4f textureTransformation() {
        return this.model.texture().texCoordTransformation();
    }
}
