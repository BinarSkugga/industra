/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.game.composer;

import com.industra.engine.Transformable2D;
import com.industra.engine.Updatable;
import lombok.Getter;
import org.joml.Vector2f;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class Entity2D extends LinkedHashSet<Component> implements Transformable2D, Updatable {
    @Getter private Vector2f positionXY;
    @Getter private Vector2f scaleXY;
    @Getter private float rotationZ;

    public Entity2D(List<Component> components) {
        super(components);
    }

    public Entity2D(Component... components) {
        this(Arrays.asList(components));
    }

    public Entity2D(Vector2f positionXY, Vector2f scaleXY, float rotationZ, Component... components) {
        this(Arrays.asList(components));

        this.positionXY = positionXY;
        this.scaleXY = scaleXY;
        this.rotationZ = rotationZ;
    }

    public boolean hasComponent(Class<? extends Component> clazz) {
        for(Component c : this) {
            if(c.getClass().equals(clazz))
                return true;
        }
        return false;
    }

    public <T extends Component> T getComponent(Class<T> clazz) {
        for(Component c : this) {
            if(c.getClass().equals(clazz)) {
                @SuppressWarnings("unchecked")
                T component = (T) c;
                return component;
            }
        }
        throw new ComponentNotFoundException();
    }

    public boolean addAll(Component... components) {
        return this.addAll(Arrays.asList(components));
    }

    @Override
    public void transform(Transformable2D transformer) {
        this.positionXY = transformer.positionXY();
        this.rotationZ = transformer.rotationZ();
        this.scaleXY = transformer.scaleXY();
    }
}
