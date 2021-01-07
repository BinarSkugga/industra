/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.composer.impl.input;

import com.industra.engine.input.InputList;
import com.industra.engine.input.Key;
import com.industra.game.Turret;
import com.industra.game.composer.AbstractInputComponent;
import com.industra.game.composer.impl.CollisionComponent;
import org.jbox2d.common.Vec2;

public class TurretInputComponent extends AbstractInputComponent<Turret> {
    private Turret turret;
    private CollisionComponent cc;

    public TurretInputComponent(Turret parent) {
        super(parent);
        this.turret = parent;
        this.cc = parent.getComponent(CollisionComponent.class);
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
        if(pressed.has(Key.UP)) {
            if(this.turret.energy + 5 <= 100)
                this.turret.energy += 5;
            else
                this.turret.energy = 100;
        }
        else if(pressed.has(Key.DOWN)) {
            if(this.turret.energy - 5 >= 0)
                this.turret.energy -= 5;
            else
                this.turret.energy = 0;
        } else if(held.has(Key.UP)) {
            if(this.turret.energy + 1 <= 100)
                this.turret.energy += 1;
            else
                this.turret.energy = 100;
        } else if(held.has(Key.DOWN)) {
            if(this.turret.energy - 1 >= 0)
                this.turret.energy -= 1;
            else
                this.turret.energy = 0;
        }

        Vec2 movingVector = new Vec2(0.0f, 0.0f);
        if (held.has(Key.W) || pressed.has(Key.W))
            movingVector.y = -1;
        else if (held.has(Key.S) || pressed.has(Key.S))
            movingVector.y = 1;
        if (held.has(Key.A) || pressed.has(Key.A))
            movingVector.x = -1;
        else if (held.has(Key.D) || pressed.has(Key.D))
            movingVector.x = 1;
        movingVector.normalize();

        this.cc.box().push(movingVector);
    }
}
