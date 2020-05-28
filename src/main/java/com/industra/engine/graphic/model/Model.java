/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.model;

import com.industra.engine.Disposable;
import com.industra.engine.graphic.Drawable;
import com.industra.engine.graphic.Texturable;
import com.industra.utils.Logger;
import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.opengl.GL40;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Model implements Disposable, Drawable {
    @Getter private VertexArray va;
    @Getter private int indicesCount;

    public Model(@NonNull float[] vertices, @NonNull int[] indices, float[] texCoords) {
        this.va = new VertexArray();
        this.indicesCount = indices.length;

        this.va.bind();
        this.va.addIndices(new IntVertexBuffer(indices));
        this.va.addFloat(new FloatVertexBuffer(vertices));
        if (texCoords != null)
            this.va.addFloat(new FloatVertexBuffer(texCoords));
        this.va.unbind();
    }

    public Model(@NonNull float[] vertices, @NonNull int[] indices) {
        this(vertices, indices, null);
    }

    public static Model load(@NonNull String model) {
        try {
            InputStream modelStream = Model.class.getClassLoader().getResourceAsStream("models/" + model + ".model");
            Scanner scanner = new Scanner(modelStream);
            ArrayList<String> lines = new ArrayList<>();
            while (scanner.hasNext()) lines.add(scanner.next());
            scanner.close();

            int verticesCount = (int) lines.stream().filter(l -> l.startsWith("V")).count();
            float[] vertices = new float[verticesCount * 2];

            String[] brokenIndices = lines.stream().filter(l -> l.startsWith("I")).findFirst().get()
                    .split(":")[1].split(",");
            int[] indices = new int[brokenIndices.length];
            for (int i = 0; i < brokenIndices.length; i++) {
                indices[i] = Integer.parseInt(brokenIndices[i]);
            }

            for (int i = 0, j = 0; j < lines.size(); i++, j++) {
                String[] broken = lines.get(j).split(":");
                if (broken[0].equals("V")) {
                    broken = broken[1].trim().split(",");
                    vertices[i++] = Float.parseFloat(broken[0]);
                    vertices[i] = Float.parseFloat(broken[1]);
                } else {
                    i--;
                }
            }

            int texCoordsCount = (int) lines.stream().filter(l -> l.startsWith("T")).count();
            float[] texCoords = new float[texCoordsCount * 2];

            for (int i = 0, j = 0; j < lines.size(); i++, j++) {
                String[] broken = lines.get(j).split(":");
                if (broken[0].equals("T")) {
                    broken = broken[1].trim().split(",");
                    texCoords[i++] = Float.parseFloat(broken[0]);
                    texCoords[i] = Float.parseFloat(broken[1]);
                } else {
                    i--;
                }
            }

            if (texCoords.length == vertices.length)
                return new Model(vertices, indices, texCoords);
            return new Model(vertices, indices);
        } catch (Exception e) {
            Logger.error(e);
            Logger.error("Couldn't load model." + model);
        }
        return null;
    }

    @Override
    public void draw(Texturable texture) {
        this.va.bind();
        for (int i = 0; i < this.va.buffers().size(); i++) GL40.glEnableVertexAttribArray(i);
        if (texture != null) {
            GL40.glActiveTexture(GL40.GL_TEXTURE0);
            texture.bind();
        }
        GL40.glDrawElements(GL40.GL_TRIANGLES, this.indicesCount, GL40.GL_UNSIGNED_INT, 0);
        for (int i = 0; i < this.va.buffers().size(); i++) GL40.glDisableVertexAttribArray(i);
    }

    @Override
    public void dispose() {
        this.va.dispose();
    }
}
