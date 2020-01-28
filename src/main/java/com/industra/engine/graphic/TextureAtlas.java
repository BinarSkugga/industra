/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import lombok.Getter;
import org.joml.Vector2i;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextureAtlas {
    @Getter private String name;
    @Getter private List<Texture> textures;

    public TextureAtlas(String name, TextureInterpolation interpolation) {
        try {
            this.textures = new ArrayList<>();
            this.name = name;
            InputStream textureStream = Texture.class.getClassLoader().getResourceAsStream("textures/" + name + ".png");
            BufferedImage data = ImageIO.read(textureStream);

            InputStream atlasStream = Model.class.getClassLoader().getResourceAsStream("textures/" + name + ".atlas");
            Scanner scanner = new Scanner(atlasStream);
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] broken = line.split(":");
                String subName = broken[0];

                broken = broken[1].split("/");
                String[] brokenPosition = broken[0].split(",");
                Vector2i position = new Vector2i(Integer.parseInt(brokenPosition[0]), Integer.parseInt(brokenPosition[1]));
                int size = Integer.parseInt(broken[1]);

                BufferedImage subTextureData = data.getSubimage(position.x, position.y, size, size);
                this.textures.add(new Texture(subName, subTextureData, interpolation));
            }
            scanner.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public TextureAtlas(String name) {
        this(name, TextureInterpolation.NEAREST);
    }

    public Texture getByName(String name) {
        return this.textures.parallelStream().filter(t -> t.name().equals(name)).findFirst().get();
    }

    public Texture[] texturesArray() {
        Texture[] array = new Texture[this.textures.size()];
        this.textures.toArray(array);
        return array;
    }
}
