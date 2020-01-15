/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import com.binarskugga.Constants;
import com.binarskugga.utils.Logger;
import lombok.NonNull;
import lombok.Synchronized;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class InputTracker {
    private static InputTracker tracker;
    private Map<Integer, Integer> trackedStates = new HashMap<>();
    private Set<Method> callbacks;

    private InputTracker() {
        Reflections ref = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.binarskugga"))
                .setScanners(new MethodAnnotationsScanner())
        );
        this.callbacks = ref.getMethodsAnnotatedWith(OnInput.class);

        int[] tracked = this.callbacks.stream()
                .flatMapToInt(m -> Arrays.stream(m.getAnnotation(OnInput.class).keys()))
                .distinct().toArray();

        for(int input : tracked) {
            this.trackedStates.put(input, 0);
        }
    }

    public void update(long window) {
        for(Map.Entry<Integer, Integer> state : this.trackedStates.entrySet()) {
            int glfwState = glfwGetKey(window, state.getKey());
            int previousState = state.getValue();

            if(glfwState == Constants.INPUT_IDLE) {
                if(previousState == Constants.INPUT_PRESSED) {
                    this.trackedStates.put(state.getKey(), Constants.INPUT_RELEASED);
                    Logger.out(state.getKey() + " released");
                } else {
                    this.trackedStates.put(state.getKey(), Constants.INPUT_IDLE);
                }
            } else {
                this.trackedStates.put(state.getKey(), Constants.INPUT_PRESSED);
                Logger.out(state.getKey() + " pressed");
            }
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
