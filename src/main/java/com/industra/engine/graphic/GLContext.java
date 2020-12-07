/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.Disposable;
import com.industra.engine.ResourceManager;
import com.industra.engine.Window;
import com.industra.engine.graphic.shader.ShaderProgram;
import com.industra.utils.Logger;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL40.*;

public class GLContext implements Disposable {
    public void init() {
        glfwMakeContextCurrent(Window.get().window());
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Logger.out("OpenGL Version " + glGetString(GL_VERSION));
    }

    public void run() {
        glClearColor(1, 1, 1, 1);
        glClear(GL_COLOR_BUFFER_BIT);

        // RENDER & UPDATE
        ResourceManager.get().getByClass(ShaderProgram.class).forEach(ShaderProgram::render);

        glfwSwapBuffers(Window.get().window());
    }

    @Override
    public void dispose() {
        GL.destroy();
    }
}
