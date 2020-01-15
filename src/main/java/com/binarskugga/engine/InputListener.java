/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import java.util.List;

public interface InputListener {
    default void onKeyboardInput(List<Integer> pressed, List<Integer> released, List<Integer> idle) {}
    default void onMouseInput() {} // TODO: actually use this
}
