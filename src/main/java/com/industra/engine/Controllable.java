/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine;

import com.industra.engine.input.InputList;

public interface Controllable {
    default void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
    }

    default void onMouseInput() {
    } // TODO: actually use this
}
