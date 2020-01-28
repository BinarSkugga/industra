/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine;

import com.industra.engine.graphic.Texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceManager implements Disposable {
    private static ResourceManager instance;
    private List<Disposable> resources;

    private ResourceManager() {
        this.resources = new ArrayList<>();
    }

    public static ResourceManager get() {
        if(instance == null)
            instance = new ResourceManager();
        return instance;
    }

    public void register(Disposable... elements) {
        this.resources.addAll(Arrays.asList(elements));
    }

    public void unregister(Disposable... elements) {
        this.resources.removeAll(Arrays.asList(elements));
    }

    public <T extends Disposable> List<T> getByClass(Class<T> clazz) {
        return this.resources.parallelStream()
                .filter(e -> clazz.isAssignableFrom(e.getClass()))
                .map(clazz::cast).collect(Collectors.toList());
    }

    public <T extends Disposable> Texture getTexture(String name) {
        return this.resources.parallelStream()
                .filter(e -> Texture.class.isAssignableFrom(e.getClass()) && ((Texture) e).name().equals(name))
                .map(Texture.class::cast).findFirst().get();
    }

    @Override
    public void dispose() {
        for(Disposable element : this.resources)
            element.dispose();
    }
}
