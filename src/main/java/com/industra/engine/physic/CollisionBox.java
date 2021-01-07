/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.physic;

import com.industra.engine.Transformable2D;
import com.industra.engine.graphic.Material;
import com.industra.engine.Transformable;
import lombok.Getter;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CollisionBox implements Transformable2D {
    private Vector2f size;

    @Getter private PolygonShape shape;
    @Getter private BodyDef bodyDef;
    @Getter private Material material;

    @Getter private Body body;
    @Getter private Fixture fixture;

    public CollisionBox(Vector2f size, Material material) {
        this.size = size;
        this.material = material;

        this.bodyDef = new BodyDef();
        this.bodyDef.type = this.material.type();
    }

    private void createShape(Vector2f size) {
        this.size = size;
        this.shape = new PolygonShape();
        this.shape.setAsBox(size.x / 2, size.y / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = this.material.friction();
        fixtureDef.density = this.material.density();
        fixtureDef.restitution = this.material.restitution();
        fixtureDef.shape = this.shape;
        this.fixture = this.body.createFixture(fixtureDef);
    }

    public void spawn(Body body, Vector2f position, float angle) {
        this.body = body;
        this.createShape(this.size);
        this.body.setTransform(new Vec2(position.x, position.y), angle);
    }

    @Override
    public void transform(Transformable2D transformer) {
        this.body.destroyFixture(this.fixture);
        this.createShape(transformer.scaleXY());

        this.body.setTransform(new Vec2(transformer.positionXY().x, transformer.positionXY().y), transformer.rotationZ());
    }

    public void push(Vec2 force) {
        force.mulLocal(World.FORCE_CONSTANT);
        this.body.applyForceToCenter(force);
    }

    public void torque(float torque) {
        this.body.applyTorque(torque * World.FORCE_CONSTANT);
    }

    public void translate(Vector2f vector) {
        Vec2 newPosition = this.body.getPosition().add(new Vec2(vector.x, vector.y));
        this.body.setTransform(newPosition, (float) Math.toRadians(this.body.getAngle()));
    }

    public void rotate(float angle) {
        float newAngle = this.body.getAngle() + angle;
        this.body.setTransform(this.body.getPosition(), (float) Math.toRadians(newAngle));
    }

    @Override
    public Vector2f positionXY() {
        Vec2 position = this.body.getPosition();
        return new Vector2f(position.x, position.y);
    }

    @Override
    public Vector2f scaleXY() {
        return new Vector2f(this.size.x, this.size.y);
    }

    @Override
    public float rotationZ() {
        return this.body.getAngle();
    }
}
