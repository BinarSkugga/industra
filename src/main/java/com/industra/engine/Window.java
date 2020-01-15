/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine;

import com.industra.engine.graphic.GLContext;
import com.industra.engine.input.InputTracker;
import com.industra.utils.Clock;
import com.industra.utils.Logger;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static org.lwjgl.glfw.GLFW.*;

public class Window implements Disposable {
    private Clock clock = new Clock();
    @Getter private long window;
    @Getter private int width, height;
    @Getter private String title;

    @Setter private GLContext context;

    public Window(int width, int height, @NonNull String title) {
        // TODO: Assign FPS, width and height from dynamic properties
        this.width = width;
        this.height = height;
        this.title = title;
        this.clock.calibrate(60);
    }

    public Window title(@NonNull String title) {
        this.title = title;
        glfwSetWindowTitle(this.window, title);
        return this;
    }

    public void init() {
        if (!glfwInit()) Logger.error("GLFW failed to initialize.", 1);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
        Logger.out("GLFW Version " + GLFW_VERSION_MAJOR + "." + GLFW_VERSION_MINOR + "." + GLFW_VERSION_REVISION);

        this.window = glfwCreateWindow(this.width, this.height, this.title, 0, 0);

        glfwMakeContextCurrent(this.window);
        this.context.init();
    }

    public void run() {
        while (!glfwWindowShouldClose(this.window)) {
            this.clock.tick();

            this.context.run();
            glfwSwapBuffers(this.window);
            glfwPollEvents();
            InputTracker.get().update(this.window);

            this.clock.tock();
        }
    }

    @Override
    public void dispose() {
        this.context.dispose();

        glfwDestroyWindow(this.window);
        glfwTerminate();
    }
}
