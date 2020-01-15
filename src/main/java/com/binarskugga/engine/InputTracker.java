/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import com.binarskugga.Constants;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class InputTracker {
    private static InputTracker tracker;
    private Map<Integer, Integer> trackedStates = new HashMap<>();
    private List<InputListener> listeners = new ArrayList<>();

    private InputTracker() {
        List<Integer> tracked = Arrays.asList(Key.class.getDeclaredFields()).stream()
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
            int glfwState = glfwGetKey(window, state.getKey());
            int previousState = state.getValue();

            if(glfwState == Constants.INPUT_IDLE && glfwState != previousState) {
                if(previousState == Constants.INPUT_PRESSED) {
                    this.trackedStates.put(state.getKey(), Constants.INPUT_RELEASED);
                } else {
                    this.trackedStates.put(state.getKey(), Constants.INPUT_IDLE);
                }
            } else {
                if(glfwState != previousState) this.trackedStates.put(state.getKey(), Constants.INPUT_PRESSED);
            }
        }

        for(InputListener listener : this.listeners) {
            List<Integer> pressed = new ArrayList<>();
            List<Integer> released = new ArrayList<>();
            List<Integer> idle = new ArrayList<>();

            for(Map.Entry<Integer, Integer> keyState : this.trackedStates.entrySet()) {
                if(keyState.getValue() == Constants.INPUT_IDLE) idle.add(keyState.getKey());
                else if(keyState.getValue() == Constants.INPUT_PRESSED) pressed.add(keyState.getKey());
                else if(keyState.getValue() == Constants.INPUT_RELEASED) released.add(keyState.getKey());
            }

            listener.onKeyboardInput(pressed, released, idle);
        }
    }

    @Synchronized
    public static InputTracker get() {
        if(tracker == null) {
            tracker = new InputTracker();
        }
        return tracker;
    }
}
