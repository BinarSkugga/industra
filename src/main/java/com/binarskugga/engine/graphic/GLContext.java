/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine.graphic;

import com.binarskugga.Constants;
import com.binarskugga.engine.Disposable;
import com.binarskugga.engine.Window;
import com.binarskugga.utils.Clock;
import com.binarskugga.utils.Logger;
import lombok.NonNull;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class GLContext implements Disposable {
    private Clock clock = new Clock();
    private Window window;
    private Model model;

    public GLContext(@NonNull Window window) {
        this.window = window;
        this.clock.calibrate(60);
    }

    public GLContext(@NonNull Window window, int fps) {
        this.window = window;
        this.clock.calibrate(fps);
    }

    private void create2dGraphics() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, this.window.width(), this.window.height(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    public void init() {
        GL.createCapabilities();
        this.create2dGraphics();

        Logger.out("OpenGL Version " + glGetString(GL_VERSION));

        this.model = Model.load("square");
    }

    public void run() {
        this.clock.tick();

        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        // RENDER & UPDATE
        this.model.draw();

        GL11.glPopMatrix();

        this.window.title(Constants.GAME_TITLE + " - FPS:" + this.clock.fps());
        this.clock.tock();
    }

    @Override
    public void dispose() {
        this.model.dispose();
        GL.destroy();
    }
}
