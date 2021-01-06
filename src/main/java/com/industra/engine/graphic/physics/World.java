/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.physics;

import com.industra.engine.graphic.Updatable;
import com.industra.engine.input.InputTracker;
import lombok.Getter;
import lombok.Synchronized;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.JointDef;
import org.joml.Vector2f;

import java.util.ArrayList;

public class World {
    private static World instance;

    @Getter private final org.jbox2d.dynamics.World b2dworld;
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

    public void update(long window, float delta, int velocityIter, int positionIter) {
        this.b2dworld.step(delta, velocityIter, positionIter);
        InputTracker.get().update(window);
        this.updatables.parallelStream().forEach(u -> {
            u.update(this);
        });
    }

    public void update(long window, float delta) {
        this.update(window, delta, 6, 8);
    }

    public void addJoint(JointDef joint) {
        this.joints.add(joint);
    }

    public void createJoints(float delta) {
        this.b2dworld.step(delta, 0, 0);
        this.joints.forEach(this.b2dworld::createJoint);
    }
}
