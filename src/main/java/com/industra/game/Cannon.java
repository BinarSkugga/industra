/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Controllable;
import com.industra.engine.graphic.model.TexturedModel;
import com.industra.engine.physic.CollisionBox;
import com.industra.engine.physic.World;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.physic.materials.BaseMaterial;
import com.industra.game.composer.Entity2D;
import com.industra.game.composer.impl.CollisionComponent;
import com.industra.game.composer.impl.TexturedModelComponent;
import com.industra.game.composer.impl.input.CannonInputComponent;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.joml.Vector2f;

public class Cannon extends Entity2D implements Controllable {
    public Cannon() {
        Texture texture = new Texture("cannon_001").animated(false);
        CollisionBox collisionBox = new CollisionBox(new Vector2f(60f), new BaseMaterial());

        this.addAll(
                new TexturedModelComponent(this, TexturedModel.load("square", texture)),
                new CollisionComponent(this, collisionBox)
        );

        this.add(new CannonInputComponent(this));
    }

    public Cannon turret(Turret turret) {
        CollisionComponent cc = this.getComponent(CollisionComponent.class);

        RevoluteJointDef revoluteJD = new RevoluteJointDef();
        revoluteJD.bodyA = turret.getComponent(CollisionComponent.class).box().body();
        revoluteJD.bodyB = cc.box().body();
        revoluteJD.collideConnected = false;
        revoluteJD.localAnchorA.set(0, 0);
        revoluteJD.localAnchorB.set(0, 0);
        World.get().addJoint(revoluteJD);

        return this;
    }
}
