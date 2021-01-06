/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Entity;
import com.industra.engine.graphic.Texturable;
import com.industra.engine.graphic.model.Model;
import com.industra.engine.graphic.physics.CollisionBox;
import com.industra.engine.graphic.physics.World;
import com.industra.engine.graphic.texture.Texture;
import com.industra.game.components.BaseShaderComponent;
import com.industra.game.components.CannonControlComponent;
import com.industra.game.components.CollisionComponent;
import com.industra.game.components.SpriteComponent;
import lombok.Getter;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.joml.Vector2f;

public class Cannon extends Entity {
    public Cannon(Model model, Texture texture, CollisionBox box) {
        this.add(new SpriteComponent(this, model, texture));

        CollisionComponent collision = new CollisionComponent(this, box);
        collision.box().fixture().setFriction(500f);

        CannonControlComponent control = new CannonControlComponent(this, collision);
        this.addAll(collision, control);
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

        this.add(new BaseShaderComponent(this) {
            @Override
            public Texture texture() {
                return this.parent.getClassComponent(SpriteComponent.class).texture();
            }

            @Override
            public Vector2f position() {
                return ((Cannon) this.parent).turret().position().add(this.parent.collisionBox.position());
            }

            @Override
            public float rotationZ() {
                return this.parent.collisionBox.rotation().z;
            }

            @Override
            public float scaleXY() {
                return this.collisionBox.scale().x;
            }
        });

        return this;
    }
}
