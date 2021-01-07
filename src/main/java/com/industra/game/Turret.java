/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.game;

import com.industra.engine.graphic.model.TexturedModel;
import com.industra.engine.physic.CollisionBox;
import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.Controllable;
import com.industra.engine.physic.materials.BaseMaterial;
import com.industra.game.composer.Entity2D;
import com.industra.game.composer.impl.CollisionComponent;
import com.industra.game.composer.impl.TexturedModelComponent;
import com.industra.game.composer.impl.input.TurretInputComponent;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;


public class Turret extends Entity2D implements Controllable {
    @Getter @Setter public float energy = 0;

    public Turret() {
        Texture texture = new Texture("turret_001").animated(false);
        CollisionBox collisionBox = new CollisionBox(new Vector2f(60f), new BaseMaterial());

        this.addAll(
                new TexturedModelComponent(this, TexturedModel.load("square", texture)),
                new CollisionComponent(this, collisionBox)
        );

        this.add(new TurretInputComponent(this));
    }
}
