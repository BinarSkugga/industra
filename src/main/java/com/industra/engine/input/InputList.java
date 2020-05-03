/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.input;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputList extends ConcurrentLinkedQueue<Integer> {
    public boolean has(Integer key) {
        return this.contains(key);
    }

    public boolean all(Integer... keys) {
        return this.containsAll(Arrays.asList(keys));
    }

    public boolean any(Integer... keys) {
        for (Integer e : keys) {
            if (this.contains(e)) return true;
        }
        return false;
    }
}
