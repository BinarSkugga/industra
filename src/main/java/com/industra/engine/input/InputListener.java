/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.input;

public interface InputListener {
    default void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
    }

    default void onMouseInput() {
    } // TODO: actually use this
}
