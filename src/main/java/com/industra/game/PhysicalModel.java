/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.graphic.Drawable;
import com.industra.engine.graphic.SimplifiedTransformable;
import com.industra.engine.graphic.model.Model;
import com.industra.engine.physic.CollisionBox;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import com.industra.utils.Clock;
import lombok.Getter;
import org.joml.Vector2f;

public class PhysicalModel implements InputListener, Drawable, Disposable, SimplifiedTransformable {
    // Pixel per second
    private float speed = 200f;
    // Complete rotation per second
    private float rotationSpeed = 1.5f;

    private boolean moving = false;
    private boolean running = false;
    private float runningMultiplicator = 3;

    @Getter private Model model;
    @Getter private CollisionBox collisionBox;
    @Getter private Texture texture;

    public PhysicalModel(Model model, CollisionBox box, Texture texture) {
        InputTracker.get().subscribe(this);
        this.model = model;
        this.collisionBox = box;
        this.texture = texture;
    }

    @Override
    public Vector2f position() {
        return this.collisionBox.position();
    }

    @Override
    public float rotationZ() {
        return this.collisionBox.rotation().z;
    }

    @Override
    public float scaleXY() {
        return this.collisionBox.scale().x;
    }

    @Override
    public void draw() {
        this.model.draw(this.texture);
    }

    @Override
    public void dispose() {
        this.model.dispose();
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
        if (dpressed.any(Key.W, Key.S, Key.A, Key.D))
            this.running = true;
        if (pressed.any(Key.W, Key.S, Key.A, Key.D))
            this.moving = true;

        if (idle.all(Key.W, Key.S, Key.A, Key.D)) {
            this.moving = false;
            this.running = false;
        }

        if(this.moving)
            this.texture.frameTime(300);
        if(this.running)
            this.texture.frameTime(100);

        this.texture.animated(this.moving || this.running);
        this.texture.line(0);
//        if (held.has(Key.W) || pressed.has(Key.W))
//            this.texture.line(3);
//        if (held.has(Key.S) || pressed.has(Key.S))
//            this.texture.line(0);
//        if (held.has(Key.A) || pressed.has(Key.A))
//            this.texture.line(1);
//        if (held.has(Key.D) || pressed.has(Key.D))
//            this.texture.line(2);

        Vector2f movingVector = new Vector2f(0.0f, 0.0f);
        if (held.has(Key.W) || pressed.has(Key.W))
            movingVector.y = -1;
        else if (held.has(Key.S) || pressed.has(Key.S))
            movingVector.y = 1;
        if (held.has(Key.A) || pressed.has(Key.A))
            movingVector.x = -1;
        else if (held.has(Key.D) || pressed.has(Key.D))
            movingVector.x = 1;

        if (held.has(Key.Q))
            this.collisionBox.rotate(-Clock.relativize(this.rotationSpeed * 360f));
        else if (held.has(Key.E))
            this.collisionBox.rotate(Clock.relativize(this.rotationSpeed * 360f));

        if (held.has(Key.Z))
            this.collisionBox.scale().add(this.rotationSpeed, this.rotationSpeed);
        else if(held.has(Key.X))
            this.collisionBox.scale().sub(this.rotationSpeed, this.rotationSpeed);

        if (!movingVector.equals(0, 0)) {
            movingVector.normalize(movingVector);
            movingVector.mul(Clock.relativize(this.speed), movingVector);

            if (this.running)
                movingVector.mul(this.runningMultiplicator, movingVector);

            this.collisionBox.translate(movingVector);
        }
    }
}
