/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic;

import com.industra.engine.Disposable;
import com.industra.utils.Logger;
import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Model implements Disposable, Drawable {
    @Getter private VertexArray va;
    @Getter private int indicesCount;

    public Model(@NonNull float[] vertices, @NonNull int[] indices) {
        this.va = new VertexArray();
        this.indicesCount = indices.length;

        this.va.bind();
        this.va.addIndices(new IntVertexBuffer(indices));
        this.va.addFloat(new FloatVertexBuffer(vertices));
        this.va.unbind();
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
            return new Model(vertices, indices);
        } catch (Exception e) {
            Logger.error(e);
            Logger.error("Couldn't load model." + model);
        }
        return null;
    }

    @Override
    public void draw() {
        this.va.bind();
        for (int i = 0; i < this.va.buffers().size(); i++) GL20.glEnableVertexAttribArray(i);
        GL11.glDrawElements(GL11.GL_TRIANGLES, this.indicesCount, GL11.GL_UNSIGNED_INT, 0);
        for (int i = 0; i < this.va.buffers().size(); i++) GL20.glDisableVertexAttribArray(i);
        this.va.unbind();
    }

    @Override
    public void dispose() {
        this.va.dispose();
    }
}
