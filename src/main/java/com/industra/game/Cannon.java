/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.graphic.BaseShaderable;
import com.industra.engine.graphic.Updatable;
import com.industra.engine.graphic.model.Model;
import com.industra.engine.graphic.physics.CollisionBox;
import com.industra.engine.graphic.physics.World;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import lombok.Getter;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.joml.Vector2f;

public class Cannon implements InputListener, Disposable, Updatable, BaseShaderable {
    @Getter private Model model;
    @Getter private CollisionBox collisionBox;
    @Getter private Texture texture;
    private Turret turret;

    public Cannon(Model model, CollisionBox box, Texture texture) {
        InputTracker.get().subscribe(this);
        World.get().register(this);

        this.model = model;
        this.collisionBox = box;
        this.texture = texture;
    }

    public Cannon turret(Turret turret) {
        this.turret = turret;

        RevoluteJointDef revoluteJD = new RevoluteJointDef();
        revoluteJD.bodyA = this.turret.collisionBox().body();
        revoluteJD.bodyB = this.collisionBox().body();
        revoluteJD.collideConnected = false;
        revoluteJD.localAnchorA.set(0, 0);
        revoluteJD.localAnchorB.set(0, 0);
        World.get().addJoint(revoluteJD);


        return this;
    }

    @Override
    public void update(World world) {
    }

    @Override
    public Vector2f position() {
        return this.turret.position();
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
        if(held.has(Key.Q))
            this.collisionBox.torque(-1f);
        if(held.has(Key.E))
            this.collisionBox.torque(1f);
    }
}
