/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

public class EventData {
    @Getter private String name;

    @Accessors(prefix = {"m_"}) private int m_int;
    @Accessors(prefix = {"m_"}) private float m_float;
    @Accessors(prefix = {"m_"}) private String m_string;

    @Getter @Setter private String audioPath;
    @Getter @Setter private float volume;
    @Getter @Setter private float balance;

    public EventData (@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString () {
        return this.name;
    }
}
