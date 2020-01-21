/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.graphic.Drawable;
import com.industra.engine.graphic.Model;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import com.industra.utils.Logger;
import lombok.Getter;
import org.joml.Vector2f;

public class PositionedModel implements InputListener, Drawable, Disposable {
    @Getter private Vector2f position = new Vector2f(24, 21);

    private float speed = 1f;
    private boolean running = false;
    private float runningMultiplicator = 2;

    private Model model;

    public PositionedModel() {
        InputTracker.get().subscribe(this);
        this.model = Model.load("square");
    }

    @Override
    public void draw() {
        this.model.draw();
    }

    @Override
    public void dispose() {
        this.model.dispose();
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList held, InputList released, InputList idle) {
        if(held.has(Key.W))
            this.position = this.position.add(0, -this.speed);
        if(held.has(Key.S))
            this.position = this.position.add(0, this.speed);
        if(held.has(Key.A))
            this.position = this.position.add(-this.speed, 0);
        if(held.has(Key.D))
            this.position = this.position.add(this.speed, 0);

        Logger.out("(" + this.position.x + "," + this.position.y + ")");
    }
}
