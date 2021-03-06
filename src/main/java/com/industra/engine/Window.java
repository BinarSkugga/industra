/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine;

import com.industra.Constants;
import com.industra.engine.graphic.GLContext;
import com.industra.engine.physic.World;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import com.industra.utils.Clock;
import com.industra.utils.Logger;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static org.lwjgl.glfw.GLFW.*;

public class Window implements Disposable, Controllable {
    private static Window instance;

    @Getter private long window;
    @Getter private int width, height;
    @Getter private String title;

    @Setter private GLContext context;
    @Getter private World world;

    private Window(int width, int height, @NonNull String title) {
        // TODO: Assign FPS, width and height from dynamic properties
        this.width = width;
        this.height = height;
        this.title = title;
        InputTracker.get().subscribe(this);
    }

    public static Window get() {
        if (instance == null) {
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
        this.world = new World();
        this.context.init();
    }

    public void run() {
        this.world.createJoints(Clock.deltaS());

        Clock.init(60);
        while (!glfwWindowShouldClose(this.window)) {
            Clock.sync();
            InputTracker.get().update(this.window);

            glfwPollEvents();
            this.context.run();
            this.world.update(Clock.deltaS());
        }
    }

    @Override
    public void dispose() {
        this.context.dispose();

        glfwDestroyWindow(this.window);
        glfwTerminate();
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
        if(released.has(Key.ESCAPE))
            glfwSetWindowShouldClose(this.window, true);
    }
}
