/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.physics;

import com.industra.engine.graphic.Updatable;
import lombok.Getter;
import lombok.Synchronized;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.JointDef;
import org.joml.Vector2f;

import java.util.ArrayList;

public class World {
    private static World instance;
    public final float FORCE_CONSTANT = 100000000f;
    public final float IMPULSE_CONSTANT = 5000000f;

    @Getter private final org.jbox2d.dynamics.World b2dworld;
    @Getter private final float AIR_DENSITY = 0.1f;
    private ArrayList<JointDef> joints;
    private ArrayList<Updatable> updatables;

    @Synchronized
    public static World get() {
        return instance;
    }

    public World(Vec2 gravity) {
        this.b2dworld = new org.jbox2d.dynamics.World(gravity);
        this.joints = new ArrayList<>();
        this.updatables = new ArrayList<>();
        instance = this;
    }

    public World() {
        this(new Vec2());
    }

    public World(Vector2f gravity) {
        this(new Vec2(gravity.x, gravity.y));
    }

    public World(float x, float y) {
        this(new Vec2(x, y));
    }

    public void update(float delta, int velocityIter, int positionIter) {
        this.b2dworld.step(delta, velocityIter, positionIter);
        this.updatables.forEach(u -> {
            Body body = u.collisionBox().body();

            // Apply angular air friction
            float angularVel = body.getAngularVelocity();
            float angularFriction = (angularVel * -1 * AIR_DENSITY);
            body.applyTorque(angularFriction * FORCE_CONSTANT);

            // Apply linear air friction
            Vec2 drag = body.getLinearVelocity().clone();
            drag.negateLocal(); drag.normalize();
            body.applyForceToCenter(drag.mulLocal(AIR_DENSITY * 10000f));
            System.out.println(body.getLinearVelocity() + ", " + drag);

            // Execute the update callback
            u.update(this);
        });
    }

    public void update(float delta) {
        this.update(delta, 8, 3);
    }

    public void register(Updatable updatable) {
        this.updatables.add(updatable);
    }

    public void addJoint(JointDef joint) {
        this.joints.add(joint);
    }

    public void createJoints(float delta) {
        this.b2dworld.step(delta, 0, 0);
        this.joints.forEach(this.b2dworld::createJoint);
    }
}
