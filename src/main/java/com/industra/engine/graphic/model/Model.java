/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.graphic.model;

import com.industra.engine.Disposable;
import com.industra.engine.Drawable;
import com.industra.utils.Logger;
import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.opengl.GL40;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class Model implements Disposable, Drawable {
    @Getter private String name;
    @Getter protected VertexArray va;
    @Getter protected int indicesCount;

    public Model(String name, @NonNull float[] vertices, @NonNull int[] indices) {
        this.name = name;
        this.va = new VertexArray();
        this.indicesCount = indices.length;

        this.va.bind();
        this.va.addIndices(new IntVertexBuffer(indices));
        this.va.addFloat(new FloatVertexBuffer(vertices));
    }

    public static ModelFile parseModelFile(@NonNull String model) {
        InputStream modelStream = Model.class.getClassLoader().getResourceAsStream("models/" + model + ".model");
        Scanner scanner = new Scanner(modelStream);
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNext()) lines.add(scanner.next());
        scanner.close();

        Optional<String> opName = lines.stream().filter(l -> l.startsWith("NAME")).findFirst();
        String name;
        if(opName.isPresent())
            name = opName.get().split(":")[1];
        else
            name = model;

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

        return new ModelFile(name, indices, vertices, texCoords);
    }

    public static Model load(@NonNull String model) {
        try {
            ModelFile fileData = parseModelFile(model);
            return new Model(fileData.name(), fileData.vertices(), fileData.indices());
        } catch (Exception e) {
            Logger.error(e);
            Logger.error("Couldn't load model." + model);
        }
        return null;
    }

    @Override
    public void draw() {
        this.va.bind();
        for (int i = 0; i < this.va.buffers().size(); i++) GL40.glEnableVertexAttribArray(i);
        GL40.glDrawElements(GL40.GL_TRIANGLES, this.indicesCount, GL40.GL_UNSIGNED_INT, 0);
        for (int i = 0; i < this.va.buffers().size(); i++) GL40.glDisableVertexAttribArray(i);
    }

    @Override
    public void dispose() {
        this.va.dispose();
    }
}
