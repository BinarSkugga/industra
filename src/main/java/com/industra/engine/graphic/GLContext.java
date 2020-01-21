/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.Disposable;
import com.industra.game.PositionedModel;
import com.industra.utils.Logger;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class GLContext implements Disposable {
    private BaseShader shader;

    public void init() {
        GL.createCapabilities();
        Logger.out("OpenGL Version " + glGetString(GL_VERSION));

        this.shader = new BaseShader();
        this.shader.addEntity(new PositionedModel());
    }

    public void run() {
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);

        // RENDER & UPDATE
        this.shader.render();
    }

    @Override
    public void dispose() {
        this.shader.dispose();
        GL.destroy();
    }
}
