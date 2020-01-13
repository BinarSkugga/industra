/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import com.binarskugga.utils.Logger;
import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    @Getter private long window;
    @Getter private int width, height;
    @Getter private String title;

    @Setter private GLContext context;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public Window title(String title) {
        this.title = title;
        glfwSetWindowTitle(this.window, title);
        return this;
    }

    public void init() {
        if(!glfwInit()) Logger.error("GLFW failed to initialize.", 1);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);

        Logger.out("GLFW Version " + GLFW_VERSION_MAJOR + "." + GLFW_VERSION_MINOR + "." + GLFW_VERSION_REVISION);

        this.window = glfwCreateWindow(this.width, this.height, this.title, 0, 0);

        glfwMakeContextCurrent(this.window);
        this.context.init();
    }

    public void run() {
        while(!glfwWindowShouldClose(this.window)) {
            glfwPollEvents();
            this.context.run();
            glfwSwapBuffers(this.window);
        }
    }

    public void dispose() {
        this.context.dispose();

        glfwDestroyWindow(this.window);
        glfwTerminate();
    }
}
