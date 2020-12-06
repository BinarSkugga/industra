/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.physics;

import com.industra.engine.graphic.Material;
import com.industra.engine.graphic.Transformable;
import lombok.Getter;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CollisionBox implements Transformable {
    @Getter private static World world = com.industra.engine.graphic.physics.World.get();

    @Getter private Vector2f size;
    @Getter private float angle;

    @Getter private PolygonShape shape;
    @Getter private BodyDef bodyDef;
    @Getter private Material material;

    @Getter private Body body;

    public CollisionBox(Vector2f size, Vector2f position, float angle, Material material) {
        this.size = size;
        this.angle = angle;
        this.material = material;

        this.shape = new PolygonShape();
        this.shape.setAsBox(this.size.x, this.size.y);

        this.bodyDef = new BodyDef();
        this.bodyDef.type = this.material.type();
        this.bodyDef.position.set(position.x, position.y);
        this.bodyDef.angle = this.angle;
        this.body = world.createBody(this.bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = this.material.friction();
        fixtureDef.density = this.material.density();
        fixtureDef.restitution = this.material.restitution();
        fixtureDef.shape = this.shape;
        this.body.createFixture(fixtureDef);
    }

    public CollisionBox(Vector2f size, Vector2f position, Material material) {
        this(size, position, 0f, material);
    }

    public CollisionBox(Vector2f position, Material material) {
        this(new Vector2f(1f, 1f), position, 0f, material);
    }

    public void push(Vector2f force) {
        force.mul(1000f, force);
        this.body.applyForceToCenter(new Vec2(force.x, force.y));
    }

    public void torque(float torque) {
        this.body.applyTorque(torque);
    }

    public void translate(Vector2f vector) {
        Vec2 newPosition = this.body.getPosition().add(new Vec2(vector.x, vector.y));
        this.body.setTransform(newPosition, (float) Math.toRadians(this.angle));
    }

    public void rotate(float angle) {
        this.angle += angle;
        this.body.setTransform(this.body.getPosition(), (float) Math.toRadians(this.angle));
    }

    @Override
    public Vector2f position() {
        Vec2 position = this.body.getPosition();
        return new Vector2f(position.x, position.y);
    }

    @Override
    public Vector3f rotation() {
        return new Vector3f(0f, 0f, (float) Math.toDegrees(this.body().getAngle()));
    }

    @Override
    public Vector2f scale() {
        return this.size;
    }
}
