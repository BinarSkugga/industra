/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.Disposable;
import com.industra.engine.graphic.model.Model;
import com.industra.engine.graphic.physics.CollisionBox;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.input.InputList;
import com.industra.engine.input.InputListener;
import com.industra.engine.input.InputTracker;
import com.industra.engine.input.Key;
import lombok.Getter;
import org.joml.Vector2f;


public class Turret  implements InputListener, Disposable, BaseShaderable {

    @Getter private Model model;
    @Getter private CollisionBox collisionBox;
    @Getter private Texture texture;

    @Getter private float energyLevel = 0;

    public Turret(Model model, CollisionBox box, Texture texture) {
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
        this.texture.frame((int)Math.floor(this.energyLevel * (this.texture.totalFrame()-1) / 100f));
        this.model.draw(this.texture);
    }

    @Override
    public void dispose() {
        this.model.dispose();
    }

    @Override
    public void onKeyboardInput(InputList pressed, InputList dpressed, InputList held, InputList released, InputList idle) {
        if(pressed.has(Key.UP)) {
            if(this.energyLevel + 5 <= 100)
                this.energyLevel += 5;
            else
                this.energyLevel = 100;
            System.out.println("Energy level: " + this.energyLevel);
        }
        else if(pressed.has(Key.DOWN)) {
            if(this.energyLevel - 5 >= 0)
                this.energyLevel -= 5;
            else
                this.energyLevel = 0;
            System.out.println("Energy level: " + this.energyLevel);
        } else if(held.has(Key.UP)) {
            if(this.energyLevel + 1 <= 100)
                this.energyLevel += 1;
            else
                this.energyLevel = 100;
            System.out.println("Energy level: " + this.energyLevel);
        } else if(held.has(Key.DOWN)) {
            if(this.energyLevel - 1 >= 0)
                this.energyLevel -= 1;
            else
                this.energyLevel = 0;
            System.out.println("Energy level: " + this.energyLevel);
        }
    }

}
