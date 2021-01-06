/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine;

import com.industra.engine.graphic.Drawable;
import com.industra.engine.graphic.Updatable;
import com.industra.engine.graphic.physics.World;
import com.industra.game.components.Component;

import java.util.*;

public abstract class Entity extends LinkedHashSet<Component> implements Disposable, Updatable, Drawable {
    public Entity(Collection<Component> components) {
        super(components);
    }
    public Entity(Component... components) {
        this(Arrays.asList(components));
    }
    public Entity() {
        this(new ArrayList<>());
    }

    @Override
    public void draw() {
        for(Component c : this)
            c.draw();
    }

    @Override
    public void dispose() {
        for(Component c : this)
            c.dispose();
    }

    @Override
    public void update(World world) {
        for(Component c : this)
            c.update(world);
    }

    public void addAll(Component... components) {
        Collections.addAll(this, components);
    }

    public <T extends Component> T getClassComponent(Class<T> clazz) {
        for(Component c : this) {
            if(c.getClass().equals(clazz))
                return (T) c;
        }
        return null;
    }
}
