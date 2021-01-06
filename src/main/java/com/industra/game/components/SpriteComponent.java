/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.components;

import com.industra.engine.Entity;
import com.industra.engine.graphic.model.Model;
import com.industra.engine.graphic.texture.Texture;
import lombok.Getter;

public class SpriteComponent extends Component {
    @Getter private final Model model;
    @Getter private final Texture texture;

    public SpriteComponent(Entity parent, Model model, Texture texture) {
        super(parent);
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void draw() {
        this.model.draw(this.texture);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
        this.model.dispose();
    }
}
