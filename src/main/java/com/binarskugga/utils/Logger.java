/*
 * Copyright (c) 2020 Charles Smith
 */

package com.binarskugga.utils;

public class Logger {
    public static void error(Object o) {
        System.err.println(o);
    }

    public static void error(Object o, int code) {
        System.err.println(o);
        System.exit(code);
    }

    public static void out(Object o) {
        System.out.println(o);
    }
}
