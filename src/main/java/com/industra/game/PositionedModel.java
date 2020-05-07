/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.graphic.Drawable;
import com.industra.engine.graphic.SimplifiedTransformable;
import com.industra.engine.graphic.Texturable;
import com.industra.engine.graphic.model.Model;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import com.industra.utils.Clock;
import com.industra.utils.Logger;
import lombok.Getter;
import org.joml.Vector2f;

public class PositionedModel implements InputListener, Drawable, Disposable, SimplifiedTransformable {
    @Getter private Vector2f position = new Vector2f(0, 0);
    @Getter private float rotationZ = 0;
    @Getter private float scaleXY = 30;

    // Pixel per second
    private float speed = 200f;
    // Complete rotation per second
    private float rotationSpeed = 1.5f;

    private boolean moving = false;
    private boolean running = false;
    private float runningMultiplicator = 3;

    @Getter private Model model;

    public PositionedModel(String model, Texturable texture) {
        InputTracker.get().subscribe(this);
        this.model = Model.load(model);
        if (texture != null)
            this.model.texture(texture);
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
    public void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
        Texture text = (Texture) this.model.texture();

        if (dpressed.any(Key.W, Key.S, Key.A, Key.D))
            this.running = true;
        if (pressed.any(Key.W, Key.S, Key.A, Key.D))
            this.moving = true;

        if (idle.all(Key.W, Key.S, Key.A, Key.D)) {
            this.moving = false;
            this.running = false;
        }

        if(this.moving)
            text.frameTime(300);
        if(this.running)
            text.frameTime(100);

        text.animated(this.moving || this.running);
        if (held.has(Key.W))
            text.line(3);
        if (held.has(Key.S))
            text.line(0);
        if (held.has(Key.A))
            text.line(1);
        if (held.has(Key.D))
            text.line(2);

        Vector2f movingVector = new Vector2f(0.0f, 0.0f);
        if (held.has(Key.W))
            movingVector.y = -1;
        else if (held.has(Key.S))
            movingVector.y = 1;
        if (held.has(Key.A))
            movingVector.x = -1;
        else if (held.has(Key.D))
            movingVector.x = 1;

        if (held.has(Key.Q))
            this.rotationZ -= Clock.relativize(this.rotationSpeed * 360f);
        else if (held.has(Key.E))
            this.rotationZ += Clock.relativize(this.rotationSpeed * 360f);

        if (!movingVector.equals(0, 0) && Clock.fps() > 0) {
            movingVector.normalize(movingVector);
            movingVector.mul(Clock.relativize(this.speed), movingVector);

            if (this.running)
                movingVector.mul(this.runningMultiplicator, movingVector);

            this.position = this.position.add(movingVector);
        }
    }
}
