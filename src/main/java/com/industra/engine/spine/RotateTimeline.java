/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RotateTimeline extends CurveTimeline implements BoneTimeline {
    public final int ENTRIES = 2;
    private final int PREV_TIME = -2, PREV_ROTATION = -1;
    private final int ROTATION = 1;

    @Getter private int boneIndex;
    @Getter @Setter private float[] frames;

    public RotateTimeline(int frameCount) {
        super(frameCount);
        this.frames = new float[frameCount << 1];
    }

    @Override
    public int propertyID() {
        return (TimelineType.ROTATE.ordinal() << 24) + this.boneIndex;
    }

    public void boneIndex(int value) {
        if (value < 0) throw new RuntimeException("index must be >= 0.");
        this.boneIndex = value;
    }

    public void setFrame(int index, float time, float deg) {
        index <<= 1;
        frames[index] = time;
        frames[index + ROTATION] = deg;
    }

    @Override
    public void apply (Skeleton skeleton, float lastTime, float time, List<Event> firedEvents, float alpha, MixBlend blend,
                       MixDirection direction) {
        Bone bone = skeleton.bones().get(boneIndex);
        if (!bone.active()) return;
        float[] frames = this.frames;
        if (time < frames[0]) { // Time is before first frame.
            switch (blend) {
                case SETUP:
                    bone.rotation(bone.data().rotation());
                    return;
                case FIRST:
                    float r = bone.data().rotation() - bone.rotation();
                    bone.rotation(bone.rotation() + (r - (16384 - (int)(16384.499999999996 - r / 360)) * 360) * alpha);
                    return;
            }
            return;
        }

        if (time >= frames[frames.length - ENTRIES]) { // Time is after last frame.
            float r = frames[frames.length + PREV_ROTATION];
            switch (blend) {
                case SETUP:
                    bone.rotation(bone.data().rotation() + r * alpha);
                    break;
                case FIRST:
                case REPLACE:
                    r += bone.data().rotation() - bone.rotation();
                    r -= (16384 - (int)(16384.499999999996 - r / 360)) * 360;
                case ADD:
                    bone.rotation(bone.rotation() + r * alpha);
                    break;
            }
            return;
        }

        // Interpolate between the previous frame and the current frame.
        int frame = Animation.bsearch(frames, time, ENTRIES);
        float prevRotation = frames[frame + PREV_ROTATION];
        float frameTime = frames[frame];
        float percent = getCurvePercent((frame >> 1) - 1, 1 - (time - frameTime) / (frames[frame + PREV_TIME] - frameTime));
        // scope for 'r' to prevent compile error.
        {
            float r = frames[frame + ROTATION] - prevRotation;
            r = prevRotation + (r - (16384 - (int)(16384.499999999996 - r / 360)) * 360) * percent;
            switch (blend) {
                case SETUP:
                    bone.rotation(bone.data().rotation() + (r - (16384 - (int)(16384.499999999996 - r / 360)) * 360) * alpha);
                    break;
                case FIRST:
                case REPLACE:
                    r += bone.data().rotation() - bone.rotation();
                case ADD:
                    bone.rotation(bone.rotation() + (r - (16384 - (int)(16384.499999999996 - r / 360)) * 360) * alpha);
                    break;
            }
        }
    }
}
