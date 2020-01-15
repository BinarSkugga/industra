/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.engine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnInput {
    int[] keys();
    int[] states();
}
