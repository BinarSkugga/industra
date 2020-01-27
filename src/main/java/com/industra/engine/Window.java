/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine;

import com.industra.Constants;
import com.industra.engine.graphic.GLContext;
import com.industra.engine.input.InputTracker;
import com.industra.utils.Clock;
import com.industra.utils.Logger;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static org.lwjgl.glfw.GLFW.*;

public class Window implements Disposable {
    private static Window instance;

    private Clock renderClock = new Clock();
    private Clock updateClock = new Clock();

    @Getter private long window;
    @Getter private int width, height;
    @Getter private String title;

    @Setter private GLContext context;

    private Window(int width, int height, @NonNull String title) {
        // TODO: Assign FPS, width and height from dynamic properties
        this.width = width;
        this.height = height;
        this.title = title;
        this.renderClock.calibrate(Constants.FPS_CAP);
        this.updateClock.calibrate(Constants.FPS_CAP);
    }

    public static Window get() {
        if(instance == null) {
            instance = new Window(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, Constants.GAME_TITLE);
        }
        return instance;
    }

    public Window title(@NonNull String title) {
        this.title = title;
        glfwSetWindowTitle(this.window, title);
        return this;
    }

    public void init() {
        if (!glfwInit()) Logger.error("GLFW failed to initialize.", 1);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
        Logger.out("GLFW Version " + GLFW_VERSION_MAJOR + "." + GLFW_VERSION_MINOR + "." + GLFW_VERSION_REVISION);

        this.window = glfwCreateWindow(this.width, this.height, this.title, 0, 0);
        this.context.init();
    }

    public Thread updateLoop() {
        return new Thread(() -> {
            while (!glfwWindowShouldClose(this.window)) {
                this.updateClock.tick();
                InputTracker.get().update(this.window);
                this.updateClock.tock();
            }
        });
    }

    public void run() {
        this.updateLoop().start();
        while (!glfwWindowShouldClose(this.window)) {
            this.renderClock.tick();

            this.context.run();
            glfwPollEvents();

            this.renderClock.tock();
        }
    }

    @Override
    public void dispose() {
        this.context.dispose();

        glfwDestroyWindow(this.window);
        glfwTerminate();
    }
}
