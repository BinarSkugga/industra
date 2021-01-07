/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine;

import com.industra.engine.graphic.texture.Texture;
import com.industra.engine.graphic.texture.TextureAtlas;
import com.industra.game.composer.Component;
import com.industra.game.composer.Entity2D;

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
        if (instance == null)
            instance = new ResourceManager();
        return instance;
    }

    public void register(Disposable... elements) {
        this.resources.addAll(Arrays.asList(elements));
    }

    public void unregister(Disposable... elements) {
        this.resources.removeAll(Arrays.asList(elements));
    }

    public void register(Entity2D... elements) {
        for(Entity2D e : elements) {
            for(Component c : e)
                c.register(this);
        }
    }

    public <T extends Disposable> List<T> getByClass(Class<T> clazz) {
        return this.resources.parallelStream()
                .filter(e -> clazz.isAssignableFrom(e.getClass()))
                .map(clazz::cast).collect(Collectors.toList());
    }

    public Texture getTexture(String name) {
        return this.resources.parallelStream()
                .filter(e -> Texture.class.isAssignableFrom(e.getClass()) && ((Texture) e).name().equals(name))
                .map(Texture.class::cast).findFirst().get();
    }

    public TextureAtlas getTextureAtlas(String name) {
        return this.resources.parallelStream()
                .filter(e -> TextureAtlas.class.isAssignableFrom(e.getClass()) && ((TextureAtlas) e).name().equals(name))
                .map(TextureAtlas.class::cast).findFirst().get();
    }

    public Texture getSubTexture(String atlas, String name) {
        return this.resources.parallelStream()
                .filter(e -> TextureAtlas.class.isAssignableFrom(e.getClass()) && ((TextureAtlas) e).name().equals(atlas))
                .map(TextureAtlas.class::cast).findFirst().get().getSubTexture(name);
    }

    public Texture getSubTexture(String qualifiedName) {
        String[] broken = qualifiedName.split("/");
        return this.getSubTexture(broken[0], broken[1]);
    }

    @Override
    public void dispose() {
        for (Disposable element : this.resources)
            element.dispose();
    }
}
