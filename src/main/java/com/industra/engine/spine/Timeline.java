/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import java.util.List;

public interface Timeline {
    void apply(Skeleton skeleton, float lastTime, float time, List<Event> events, float alpha, MixBlend blend, MixDirection direction);
    int propertyID();
}
