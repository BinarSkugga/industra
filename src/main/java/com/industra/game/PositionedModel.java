/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.graphic.Drawable;
import com.industra.engine.graphic.Model;
import com.industra.engine.graphic.Texture;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import lombok.Getter;
import org.joml.Vector2f;

public class PositionedModel implements InputListener, Drawable, Disposable {
    @Getter private Vector2f position;
    @Getter private float rotation = 0;

    private float speed = 1f;
    private float rotationSpeed = 6f;
    private boolean running = false;
    private float runningMultiplicator = 5;

    private Model model;

    public PositionedModel(String model, String texture, Vector2f position) {
        InputTracker.get().subscribe(this);
        this.model = Model.load(model);
        if(texture != null)
            this.model.texture(new Texture(texture));
        this.position = position;
    }

    public PositionedModel(String model, Vector2f position) {
        this(model, null, position);
    }

    public PositionedModel(String model, String texture) {
        this(model, texture, new Vector2f(24, 21));
    }

    public PositionedModel(String model) {
        this(model, null, new Vector2f(24, 21));
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
        if(dpressed.any(Key.W, Key.S, Key.A, Key.D))
            this.running = true;
        if(idle.all(Key.W, Key.S, Key.A, Key.D))
            this.running = false;

        Vector2f movingVector = new Vector2f(0.0f, 0.0f);
        if(held.has(Key.W))
            movingVector.y = -this.speed;
        else if(held.has(Key.S))
            movingVector.y = this.speed;
        if(held.has(Key.A))
            movingVector.x = -this.speed;
        else if(held.has(Key.D))
            movingVector.x = this.speed;

        if(held.has(Key.Q))
            this.rotation -= rotationSpeed;
        else if(held.has(Key.E))
            this.rotation += rotationSpeed;

        if(!movingVector.equals(0, 0)) {
            movingVector.normalize(movingVector);

            if (this.running)
                movingVector.mul(this.runningMultiplicator, movingVector);

            this.position = this.position.add(movingVector);
        }
    }
}
