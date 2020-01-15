/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.input;

import com.industra.utils.Logger;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class InputTracker {
    public static final int INPUT_KEY_RELEASED = -1;
    public static final int INPUT_KEY_IDLE = 0;
    public static final int INPUT_KEY_PRESSED = 1;
    public static final int INPUT_KEY_HELD = 2;

    private static InputTracker tracker;
    private Map<Integer, Integer> trackedStates = new HashMap<>();
    private List<InputListener> listeners = new ArrayList<>();

    private InputTracker() {
        List<Integer> tracked = Arrays.stream(Key.class.getDeclaredFields())
                .map(f -> {
                    try {
                        return (int)(f.get(null));
                    } catch (Exception e) { /* Swallowing this since it cannot fail on public static fields. */ }
                    return -1;
                }).collect(Collectors.toList());
        for(int input : tracked) {
            this.trackedStates.put(input, 0);
        }
    }

    public void subscribe(InputListener listener) {
        this.listeners.add(listener);
    }

    public void update(long window) {
        for(Map.Entry<Integer, Integer> state : this.trackedStates.entrySet()) {
            int previousState = state.getValue();
            int glfwState = glfwGetKey(window, state.getKey());

            if(glfwState != previousState) {
                if(glfwState == INPUT_KEY_IDLE) {
                    if(previousState == INPUT_KEY_PRESSED || previousState == INPUT_KEY_HELD) {
                        this.trackedStates.put(state.getKey(), INPUT_KEY_RELEASED);
                    }
                    if(previousState == INPUT_KEY_RELEASED) {
                        this.trackedStates.put(state.getKey(), INPUT_KEY_IDLE);
                    }
                } else if(glfwState == INPUT_KEY_PRESSED) {
                    if(previousState != INPUT_KEY_HELD)
                        this.trackedStates.put(state.getKey(), INPUT_KEY_PRESSED);
                }
            } else {
                if(glfwState == INPUT_KEY_PRESSED) {
                    this.trackedStates.put(state.getKey(), INPUT_KEY_HELD);
                }
            }
        }

        InputList pressed = new InputList();
        InputList held = new InputList();
        InputList released = new InputList();
        InputList idle = new InputList();

        for(Map.Entry<Integer, Integer> keyState : this.trackedStates.entrySet()) {
            if(keyState.getValue() == INPUT_KEY_IDLE) idle.add(keyState.getKey());
            else if(keyState.getValue() == INPUT_KEY_PRESSED) pressed.add(keyState.getKey());
            else if(keyState.getValue() == INPUT_KEY_HELD) held.add(keyState.getKey());
            else if(keyState.getValue() == INPUT_KEY_RELEASED) released.add(keyState.getKey());
        }

        this.listeners.parallelStream().forEach(listener -> {
            listener.onKeyboardInput(pressed, held, released, idle);
        });
    }

    @Synchronized
    public static InputTracker get() {
        if(tracker == null) {
            tracker = new InputTracker();
        }
        return tracker;
    }
}
