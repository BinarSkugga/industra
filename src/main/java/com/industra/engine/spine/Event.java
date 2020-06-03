/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Event {
    @Getter private final EventData data;
    @Getter private final float time;
    @Getter @Setter private int intValue;
    @Getter @Setter private float floatValue;
    @Getter @Setter private String stringValue;
    @Getter @Setter private float volume;
    @Getter @Setter private float balance;

    public Event (float time, @NonNull EventData data) {
        this.time = time;
        this.data = data;
    }

    @Override
    public String toString () {
        return this.data.name();
    }
}
