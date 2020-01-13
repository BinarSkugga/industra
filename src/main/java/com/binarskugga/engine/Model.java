/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import com.binarskugga.utils.Logger;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {
    @Getter private VertexArray va;
    @Getter private int indicesCount;

    public Model(float[] vertices, int[] indices) {
        this.va = new VertexArray();
        this.indicesCount = indices.length;

        this.va.bind();
        this.va.addIndices(new IntVertexBuffer(indices));
        this.va.addVertices(new FloatVertexBuffer(vertices));
        this.va.unbind();
    }

    public static Model load(String model) {
        try {
            Scanner f = new Scanner(new File("src/main/resources/models/" + model + ".model"));
            ArrayList<String> lines = new ArrayList<>();
            while (f.hasNext()) lines.add(f.next());
            f.close();

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
            Logger.error("Couldn't load model " + model);
        }
        return null;
    }

    public void draw() {
        this.va.bind();
        for (int i = 0; i < this.va.buffers().size(); i++) GL20.glEnableVertexAttribArray(i);
        GL11.glDrawElements(GL11.GL_TRIANGLES, this.indicesCount, GL11.GL_UNSIGNED_INT, 0);
        for (int i = 0; i < this.va.buffers().size(); i++) GL20.glDisableVertexAttribArray(i);
        this.va.unbind();
    }

    public void dispose() {
        this.va.dispose();
    }
}
