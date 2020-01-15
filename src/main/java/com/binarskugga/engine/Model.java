/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import com.binarskugga.utils.Logger;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Model implements Disposable, InputListener {
    @Getter private VertexArray va;
    @Getter private int indicesCount;

    public Model(@NonNull float[] vertices, @NonNull int[] indices) {
        this.va = new VertexArray();
        this.indicesCount = indices.length;

        this.va.bind();
        this.va.addIndices(new IntVertexBuffer(indices));
        this.va.addFloat(new FloatVertexBuffer(vertices));
        this.va.unbind();

        InputTracker.get().subscribe(this);
    }

    public static Model load(@NonNull String model) {
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
            Logger.error("Couldn't load model." + model);
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

    @Override
    public void dispose() {
        this.va.dispose();
    }

    @Override
    public void onKeyboardInput(List<Integer> pressed, List<Integer> released, List<Integer> idle) {
        if(pressed.containsAll(Lists.newArrayList(Key.LEFT_CONTROL, Key.S)))
            Logger.out("Model CTRL+S callback pressed");
        else {
            if(pressed.contains(Key.S))
                Logger.out("Model S callback");
            if(released.contains(Key.S))
                Logger.out("Model S callback released");
        }
    }
}
