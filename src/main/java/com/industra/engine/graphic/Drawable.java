/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

public interface Drawable {
    default void draw(Texturable texture) {};
    default void draw() {}
}
