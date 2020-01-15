/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.utils.Logger;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public abstract class Shader {
    @Getter private int id;
    @Getter private String name;
    @Getter private int type;

    public Shader(String name, int type) {
        this.name = name;
        this.type = type;
        this.load();
    }

    protected void load() {
        StringBuilder source = new StringBuilder();
        try {
            String extension;
            if(this.type == GL20.GL_FRAGMENT_SHADER) extension = "frag";
            else if(this.type == GL32.GL_GEOMETRY_SHADER) extension = "geo";
            else extension = "vert";

            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/resources/shaders/" + this.name + "." + extension));
            String line;
            while((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
            reader.close();

            this.id = GL20.glCreateShader(this.type);
            GL20.glShaderSource(this.id, source);
            GL20.glCompileShader(this.id);

            if(GL20.glGetShaderi(this.id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                Logger.error(GL20.glGetShaderInfoLog(this.id, 500));
            }
        } catch (IOException e) {
            // TODO: Don't swallow this
        }
    }
}
