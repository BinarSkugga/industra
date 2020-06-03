/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;

public class Animation {
    @Getter private String name;
    @Getter @Setter private List<Timeline> timelines;
    private HashSet<Integer> timelineIds;
    @Getter @Setter private float duration;

    public Animation(@NonNull String name, @NonNull List<Timeline> timelines, float duration) {
        this.timelineIds = new HashSet<>();
        for(Timeline t : timelines)
            this.timelineIds.add(t.propertyID());
        this.name = name;
        this.timelines = timelines;
        this.duration = duration;
    }

    public boolean hasTimeline(int id) {
        return this.timelineIds.contains(id);
    }

    public void apply (@NonNull Skeleton skeleton, float lastTime, float time, boolean loop, List<Event> events, float alpha, MixBlend blend, MixDirection direction) {
        if (loop && duration != 0) {
            time %= duration;
            if (lastTime > 0) lastTime %= duration;
        }

        List<Timeline> timelines = this.timelines;
        for (Timeline timeline : timelines)
            timeline.apply(skeleton, lastTime, time, events, alpha, blend, direction);
    }

    static int bsearch (float[] values, float target, int step) {
        int low = 0;
        int high = values.length / step - 2;
        if (high == 0) return step;
        int current = high >> 1;
        while (true) {
            if (values[(current + 1) * step] <= target)
                low = current + 1;
            else
                high = current;
            if (low == high) return (low + 1) * step;
            current = (low + high) >> 1;
        }
    }

    static int bsearch (float[] values, float target) {
        int low = 0;
        int high = values.length - 2;
        if (high == 0) return 1;
        int current = high >> 1;
        while (true) {
            if (values[current + 1] <= target)
                low = current + 1;
            else
                high = current;
            if (low == high) return (low + 1);
            current = (low + high) >> 1;
        }
    }

    static int lsearch (float[] values, float target, int step) {
        for (int i = 0, last = values.length - step; i <= last; i += step)
            if (values[i] > target) return i;
        return -1;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
