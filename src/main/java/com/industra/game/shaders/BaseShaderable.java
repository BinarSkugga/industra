/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game.shaders;

import com.industra.engine.graphic.Drawable;
import com.industra.engine.graphic.Transformable2D;
import com.industra.engine.graphic.texture.Texture;

public interface BaseShaderable extends Transformable2D, Drawable {
    Texture texture();
}
