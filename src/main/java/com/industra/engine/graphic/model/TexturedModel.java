/*
 * Copyright (c) 2021 Charles Smith
 */

package com.industra.engine.graphic.model;

import com.industra.engine.graphic.texture.Texture;
import com.industra.utils.Logger;
import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.opengl.GL40;

public class TexturedModel extends Model {
    @Getter protected Texture texture;

    public TexturedModel(String name, @NonNull float[] vertices, @NonNull int[] indices, Texture texture, float[] texCoords) {
        super(name, vertices, indices);

        this.va.addFloat(new FloatVertexBuffer(texCoords));
        this.texture = texture;
    }

    public static TexturedModel load(String model, Texture texture) {
        try {
            ModelFile fileData = parseModelFile(model);
            return new TexturedModel(fileData.name(), fileData.vertices(), fileData.indices(), texture, fileData.texCoords());
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
        if (this.texture != null) {
            GL40.glActiveTexture(GL40.GL_TEXTURE0);
            this.texture.bind();
        }
        GL40.glDrawElements(GL40.GL_TRIANGLES, this.indicesCount, GL40.GL_UNSIGNED_INT, 0);
        if (this.texture != null) {
            this.texture.unbind();
        }
        for (int i = 0; i < this.va.buffers().size(); i++) GL40.glDisableVertexAttribArray(i);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.texture.dispose();
    }
}
