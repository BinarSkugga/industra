/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.components;

import com.industra.engine.Entity;
import com.industra.engine.graphic.Transformable2D;
import com.industra.engine.graphic.texture.Texture;

public abstract class BaseShaderComponent extends Component implements Transformable2D {
    public BaseShaderComponent(Entity parent) {
        super(parent);
    }

    public abstract Texture texture();
}
