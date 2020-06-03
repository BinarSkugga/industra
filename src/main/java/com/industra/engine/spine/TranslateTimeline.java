/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TranslateTimeline extends CurveTimeline implements BoneTimeline {
    public static int ENTRIES = 3;
    protected static int PREV_TIME = -3, PREV_X = -2, PREV_Y = -1;
    protected static int X = 1, Y = 2;

    @Getter private int boneIndex;
    @Getter @Setter private float[] frames;

    public TranslateTimeline(int frameCount) {
        super(frameCount);
        this.frames = new float[frameCount * ENTRIES];
    }

    @Override
    public int propertyID() {
        return (TimelineType.TRANSLATE.ordinal() << 24) + this.boneIndex;
    }

    public void boneIndex(int value) {
        if (value < 0) throw new RuntimeException("index must be >= 0.");
        this.boneIndex = value;
    }

    public void setFrame(int index, float time, float x, float y) {
        index *= ENTRIES;
        frames[index] = time;
        frames[index + X] = x;
        frames[index + Y] = y;
    }

    @Override
    public void apply(Skeleton skeleton, float lastTime, float time, List<Event> events, float alpha, MixBlend blend, MixDirection direction) {
        Bone bone = skeleton.bones().get(boneIndex);
        if (!bone.active()) return;
        float[] frames = this.frames;
        if (time < frames[0]) { // Time is before first frame.
            switch (blend) {
            case SETUP:
                bone.x(bone.data().x());
                bone.y(bone.data().y());
                return;
            case FIRST:
                bone.x(bone.x() + (bone.data().x() - bone.x()) * alpha);
                bone.y(bone.y() + (bone.data().y() - bone.y()) * alpha);
                return;
            }
            return;
        }

        float x, y;
        if (time >= frames[frames.length - ENTRIES]) { // Time is after last frame.
            x = frames[frames.length + PREV_X];
            y = frames[frames.length + PREV_Y];
        } else {
            // Interpolate between the previous frame and the current frame.
            int frame = Animation.bsearch(frames, time, ENTRIES);
            x = frames[frame + PREV_X];
            y = frames[frame + PREV_Y];
            float frameTime = frames[frame];
            float percent = getCurvePercent(frame / ENTRIES - 1,
                1 - (time - frameTime) / (frames[frame + PREV_TIME] - frameTime));

            x += (frames[frame + X] - x) * percent;
            y += (frames[frame + Y] - y) * percent;
        }
        switch (blend) {
            case SETUP:
                bone.x(bone.data().x() + x * alpha);
                bone.y(bone.data().y() + y * alpha);
                break;
            case FIRST:
            case REPLACE:
                bone.x(bone.x() + (bone.data().x() + x - bone.x()) * alpha);
                bone.y(bone.y() + (bone.data().y() + y - bone.y()) * alpha);
                break;
            case ADD:
                bone.x(bone.x() + x * alpha);
                bone.y(bone.y() + y * alpha);
                break;
        }
    }
}
