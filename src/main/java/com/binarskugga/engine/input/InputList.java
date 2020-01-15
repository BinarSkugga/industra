/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine.input;

import java.util.ArrayList;
import java.util.Arrays;

public class InputList extends ArrayList<Integer> {
    public boolean has(Integer key) {
        return this.contains(key);
    }

    public boolean all(Integer... keys) {
        return this.containsAll(Arrays.asList(keys));
    }

    public boolean any(Integer... keys) {
        for(Integer e : keys) {
            if(this.contains(e)) return true;
        }
        return false;
    }
}
