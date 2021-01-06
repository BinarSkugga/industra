/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.graphic.model.Model;
import com.industra.engine.graphic.physics.CollisionBox;
import com.industra.engine.graphic.physics.World;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import lombok.Getter;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.joml.Vector2f;

public class Cannon implements InputListener, Disposable, BaseShaderable {
    @Getter private Model model;
    @Getter private CollisionBox collisionBox;
    @Getter private Texture texture;
    private Turret turret;

    public Cannon(Model model, CollisionBox box, Texture texture) {
        InputTracker.get().subscribe(this);
        this.model = model;
        this.collisionBox = box;
        this.texture = texture;

        this.collisionBox.fixture().setFriction(500f);
    }

    public Cannon turret(Turret turret) {
        this.turret = turret;

        RevoluteJointDef cannonJointDef = new RevoluteJointDef();
        cannonJointDef.bodyA = this.turret.collisionBox().body();
        cannonJointDef.bodyB = this.collisionBox().body();
        cannonJointDef.collideConnected = false;
        cannonJointDef.localAnchorA.set(-this.turret.collisionBox().position().x, -this.turret.collisionBox().position().y);
        cannonJointDef.localAnchorB.set(0f, 0f);
        cannonJointDef.enableMotor = true;
        World.get().addJoint(cannonJointDef);

        return this;
    }

    @Override
    public Vector2f position() {
        return this.turret.position().add(this.collisionBox.position());
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
