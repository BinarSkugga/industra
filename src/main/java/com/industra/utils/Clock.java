/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.utils;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

// TODO: This class needs some improvements
public class Clock {
    private static long NS_IN_SEC = TimeUnit.SECONDS.toNanos(1);
    private static long NS_IN_MILLIS = TimeUnit.MILLISECONDS.toNanos(1);

    private static int fpsCount = 0;
    @Getter private static int fps;
    private static long deltaCount = 0;
    private static long frameDelta = 0;
    private static long fullDelta;

    private static long tick;
    private static long frame;
    private static boolean firstFrame = true;

    public static float relativize(float speed) {
        return speed / Clock.delta();
    }

    public static long delta() {
        return Clock.fullDelta;
    }

    public static float deltaMS() {
        return Clock.fullDelta / (float)NS_IN_MILLIS;
    }

    public static float deltaS() {
        return Clock.fullDelta / (float)NS_IN_SEC;
    }

    public static long monotonic() {
        return System.nanoTime() / NS_IN_MILLIS;
    }

    public static void init(int maxFPS) {
        Clock.frame = NS_IN_SEC / maxFPS;
        Clock.tick = System.nanoTime();
    }

    public static void sync() {
        if(!Clock.firstFrame) {
            Clock.frameDelta = System.nanoTime() - Clock.tick;
            long sleep = Clock.frame - Clock.frameDelta;

            if (sleep > 0) {
                long millis = sleep / NS_IN_MILLIS;
                int nanos = (int) (sleep % NS_IN_MILLIS);
                try { Thread.sleep(millis, nanos); }
                catch (InterruptedException ignored) {
                    System.err.println("Clock interrupted, losing " + millis + "ms");
                }
            }

            Clock.fullDelta = System.nanoTime() - Clock.tick;
            Clock.tick = System.nanoTime();

            Clock.deltaCount += Clock.frameDelta + sleep;
            Clock.fpsCount += 1;

            if (Clock.deltaCount >= NS_IN_SEC) {
                Clock.deltaCount = Clock.deltaCount - NS_IN_SEC;
                Clock.fps = Clock.fpsCount;
                Clock.fpsCount = 0;
                System.out.println(Clock.fps + "fps, " + (Clock.fullDelta / NS_IN_MILLIS) + "ms");
            }
        } else {
            Clock.firstFrame = false;
        }
    }

}