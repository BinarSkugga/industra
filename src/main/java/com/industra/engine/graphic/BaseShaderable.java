/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.graphic.texture.Texture;

public interface BaseShaderable extends SimplifiedTransformable, Drawable {
    Texture texture();
}
