/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.input;

import com.industra.engine.Controllable;
import lombok.Synchronized;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public static final long INPUT_DOUBLE_TIMEOUT = TimeUnit.MILLISECONDS.toNanos(200);

    private static InputTracker tracker;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Map<Integer, Integer> trackedStates = new HashMap<>();
    private Map<Integer, Long> trackedDPressed = new HashMap<>();
    private List<Controllable> listeners = new ArrayList<>();

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

    public void subscribe(Controllable listener) {
        this.listeners.add(listener);
    }

    public void update(long window) {
        this.executor.submit(() -> {
            InputList pressed = new InputList();
            InputList dpressed = new InputList();
            InputList held = new InputList();
            InputList released = new InputList();
            InputList idle = new InputList();

            for (Map.Entry<Integer, Integer> state : this.trackedStates.entrySet()) {
                int previousState = state.getValue();
                int key = state.getKey();
                int glfwState = glfwGetKey(window, key);

                if(glfwState == GLFW_IDLE) {
                    if (previousState == INPUT_KEY_PRESSED || previousState == INPUT_KEY_HELD || previousState == INPUT_KEY_DPRESSED) {
                        this.trackedStates.put(key, INPUT_KEY_RELEASED);
                        this.trackedDPressed.put(key, System.nanoTime());
                        released.add(key);
                    } else {
                        this.trackedStates.put(key, INPUT_KEY_IDLE);
                        idle.add(key);
                    }
                } else {
                    if(previousState != INPUT_KEY_DPRESSED && System.nanoTime() - this.trackedDPressed.get(key) <= INPUT_DOUBLE_TIMEOUT) {
                        this.trackedStates.put(key, INPUT_KEY_DPRESSED);
                        this.trackedDPressed.put(key, 0L);
                        dpressed.add(key);
                    } else if(previousState == INPUT_KEY_DPRESSED || previousState == INPUT_KEY_PRESSED || previousState == INPUT_KEY_HELD) {
                        this.trackedStates.put(key, INPUT_KEY_HELD);
                        held.add(key);
                    } else {
                        this.trackedStates.put(key, INPUT_KEY_PRESSED);
                        pressed.add(key);
                    }
                }
            }

            this.listeners.parallelStream().forEach(listener -> {
                listener.onKeyboardInput(pressed, dpressed, held, released, idle);
            });
        });
    }
}
