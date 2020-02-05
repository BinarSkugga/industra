/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.input;

import lombok.Synchronized;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class InputTracker {
    public static final int GLFW_IDLE = 0;
    public static final int GLFW_PRESS = 1;

    public static final int INPUT_KEY_RELEASED = -1;
    public static final int INPUT_KEY_IDLE = 0;
    public static final int INPUT_KEY_PRESSED = 1;
    public static final int INPUT_KEY_HELD = 2;
    public static final int INPUT_KEY_DPRESSED = 3;
    public static final long INPUT_DOUBLE_TIMEOUT = TimeUnit.MILLISECONDS.toNanos(400);

    private static InputTracker tracker;
    private Map<Integer, Integer> trackedStates = new HashMap<>();
    private Map<Integer, Long> trackedDPressed = new HashMap<>();
    private List<InputListener> listeners = new ArrayList<>();

    private InputTracker() {
        List<Integer> tracked = Arrays.stream(Key.class.getDeclaredFields())
                .map(f -> {
                    try {
                        return (int) (f.get(null));
                    } catch (Exception e) { /* Swallowing this since it cannot fail on public static fields. */ }
                    return -1;
                }).collect(Collectors.toList());
        for (int input : tracked) {
            this.trackedStates.put(input, 0);
            this.trackedDPressed.put(input, 0L);
        }
    }

    @Synchronized
    public static InputTracker get() {
        if (tracker == null) {
            tracker = new InputTracker();
        }
        return tracker;
    }

    public void subscribe(InputListener listener) {
        this.listeners.add(listener);
    }

    public void update(long window) {
        for (Map.Entry<Integer, Integer> state : this.trackedStates.entrySet()) {
            int previousState = state.getValue();
            int glfwState = glfwGetKey(window, state.getKey());

            // GLFW State Changed
            if (glfwState != previousState) {
                // Key is IDLE
                if (glfwState == GLFW_IDLE) {
                    if (previousState == INPUT_KEY_PRESSED || previousState == INPUT_KEY_HELD || previousState == INPUT_KEY_DPRESSED) {
                        this.trackedStates.put(state.getKey(), INPUT_KEY_RELEASED);
                    }
                    if (previousState == INPUT_KEY_RELEASED) {
                        this.trackedStates.put(state.getKey(), INPUT_KEY_IDLE);
                    }
                }
                // Key is PRESSED but not on HELD
                else if (glfwState == GLFW_PRESS && previousState != INPUT_KEY_HELD) {
                    if (previousState == INPUT_KEY_DPRESSED) {
                        this.trackedStates.put(state.getKey(), INPUT_KEY_HELD);
                    } else if (System.nanoTime() - this.trackedDPressed.get(state.getKey()) <= INPUT_DOUBLE_TIMEOUT) {
                        this.trackedStates.put(state.getKey(), INPUT_KEY_DPRESSED);
                    } else {
                        this.trackedStates.put(state.getKey(), INPUT_KEY_PRESSED);
                        this.trackedDPressed.put(state.getKey(), System.nanoTime());
                    }
                }
            }
            // If no change and key was PRESSED or DPRESSED, put HELD state
            else if (glfwState == GLFW_PRESS) {
                this.trackedStates.put(state.getKey(), INPUT_KEY_HELD);
            }
        }

        InputList pressed = new InputList();
        InputList dpressed = new InputList();
        InputList held = new InputList();
        InputList released = new InputList();
        InputList idle = new InputList();

        for (Map.Entry<Integer, Integer> keyState : this.trackedStates.entrySet()) {
            if (keyState.getValue() == INPUT_KEY_IDLE) idle.add(keyState.getKey());
            else if (keyState.getValue() == INPUT_KEY_PRESSED) pressed.add(keyState.getKey());
            else if (keyState.getValue() == INPUT_KEY_DPRESSED) dpressed.add(keyState.getKey());
            else if (keyState.getValue() == INPUT_KEY_HELD) held.add(keyState.getKey());
            else if (keyState.getValue() == INPUT_KEY_RELEASED) released.add(keyState.getKey());
        }

        this.listeners.parallelStream().forEach(listener -> {
            listener.onKeyboardInput(pressed, dpressed, held, released, idle);
        });
    }
}
