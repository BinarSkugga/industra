/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.Bindable;
import com.industra.engine.Disposable;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextureAtlas implements Disposable, Bindable {
    @Getter private Texture texture;
    @Getter private String name;
    private Map<String, SubTexture> subTextures;

    public TextureAtlas(String name, TextureInterpolation interpolation) {
        this.name = name;
        this.texture = new Texture(name, interpolation);
        this.subTextures = new HashMap<>();

        InputStream atlasStream = Model.class.getClassLoader().getResourceAsStream("textures/" + name + ".atlas");
        Scanner scanner = new Scanner(atlasStream);
        while (scanner.hasNext()) {
            String line = scanner.next();
            String[] broken = line.split(":");
            String subName = broken[0];

            broken = broken[1].split("/");
            String[] brokenPosition = broken[0].split(",");
            Vector2f position = new Vector2f(
                    Float.parseFloat(brokenPosition[0]) / this.texture.size().x,
                    Float.parseFloat(brokenPosition[1]) / this.texture.size().y
            );
            float size = Float.parseFloat(broken[1]) / this.texture.size().x;

            this.subTextures.put(subName, new SubTexture(this, position, size));
        }
        scanner.close();
    }

    public TextureAtlas(String name) {
        this(name, TextureInterpolation.NEAREST);
    }

    public SubTexture getSubTexture(String name) {
        return this.subTextures.get(name);
    }

    @Override
    public void bind() {
        this.texture.bind();
    }

    @Override
    public void unbind() {
        this.texture.unbind();
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
