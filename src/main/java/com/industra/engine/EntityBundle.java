/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine;

import lombok.Getter;
import org.jbox2d.dynamics.joints.Joint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

public class EntityBundle extends LinkedHashSet<Entity> implements Disposable {
    @Getter private ArrayList<Joint> joints = new ArrayList<>();

    public EntityBundle(Collection<? extends Entity> entities) {
        super(entities);
    }

    public EntityBundle(Entity... entities) {
        super(Arrays.asList(entities));
    }

    @Override
    public void dispose() {
        for(Entity entity : this)
            entity.dispose();
    }
}
