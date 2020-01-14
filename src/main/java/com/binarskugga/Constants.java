/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga;

import static org.lwjgl.glfw.GLFW.*;

public class Constants {
    public static final String GAME_TITLE = "Industra";

    public static final int INPUT_IDLE = 0;
    public static final int INPUT_PRESSED = 1;
    public static final int INPUT_RELEASED = -1;

    // Will probably need to be dynamic to allow customization of input mapping
    public static final int[] TRACKED_INPUT = new int[] {
            GLFW_KEY_W,
            GLFW_KEY_A,
            GLFW_KEY_S,
            GLFW_KEY_D,
    };
}
