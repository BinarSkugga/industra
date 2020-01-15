/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine.input;

public interface InputListener {
    default void onKeyboardInput(InputList pressed, InputList released, InputList idle) {}
    default void onMouseInput() {} // TODO: actually use this
}
