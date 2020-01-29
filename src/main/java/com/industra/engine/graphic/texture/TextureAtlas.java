/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.texture;

import com.industra.engine.Bindable;
import com.industra.engine.Disposable;
import com.industra.engine.graphic.model.Model;
import lombok.Getter;
import org.joml.Vector2f;

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
                    Float.parseFloat(brokenPosition[0]) / this.texture.imageSize().x,
                    Float.parseFloat(brokenPosition[1]) / this.texture.imageSize().y
            );

            if(broken[1].contains("x")) {
                String[] brokenSize = broken[1].split("x");
                Vector2f size = new Vector2f(
                        Float.parseFloat(brokenSize[0]) / this.texture.imageSize().x,
                        Float.parseFloat(brokenSize[1]) / this.texture.imageSize().x);

                SubTexture sub = new SubTexture(this, position, size);
                if(broken.length > 2 && broken[2].equals("animated"))
                    sub.animated(true);
                this.subTextures.put(subName, sub);
            } else {
                float size = Float.parseFloat(broken[1]) / this.texture.imageSize().x;
                this.subTextures.put(subName, new SubTexture(this, position, size));
            }
        }
        scanner.close();
    }

    public TextureAtlas(String name) {
        this(name, TextureInterpolation.NEAREST);
    }

    public SubTexture getSubTexture(String name) {
        return this.subTextures.get(name);
    }

    public SubTexture getSubTexture(Vector2f position, float size) {
        position.x /= this.texture.size().x;
        position.y /= this.texture.size().y;
        return new SubTexture(this, position, size / this.texture.size().x);
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
