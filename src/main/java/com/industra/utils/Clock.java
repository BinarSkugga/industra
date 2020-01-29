/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.utils;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

// TODO: This class needs some improvements
public class Clock {
    private static long MS_IN_SEC = TimeUnit.SECONDS.toMillis(1);

    private static long time = 0;
    @Getter static private int initialFPS = 0;
    private static long frameTime = 0;
    @Getter static float deltaTime = 0;
    @Getter static private int fps = 0;
    @Getter static private long ms = 0;
    private static int fpsCount = 0;
    private static long msCount = 0;

    public static long monotonic() {
        return System.nanoTime() / 1000000;
    }

    public static long delta() {
        return monotonic() - time;
    }

    public static float relativize(float speed) {
        return speed * Clock.deltaTime;
    }

    public static void sync(int fps) {
        if(Clock.time > 0 && Clock.frameTime > 0) {
            try {
                long waitingTime = Clock.frameTime - delta();
                if (waitingTime < 0) waitingTime = 0;
                TimeUnit.MILLISECONDS.sleep(waitingTime);
            } catch (InterruptedException e) {
                Logger.error("Clock skipped a frame.");
            }

            if (Clock.msCount >= MS_IN_SEC) {
                Clock.fps = Clock.fpsCount;
                Clock.ms = Clock.msCount;

                Clock.fpsCount = 0;
                Clock.msCount = Clock.msCount - Clock.MS_IN_SEC;

                Clock.deltaTime = delta() / 1000f;
                Logger.out("FPS: " + Clock.fps);
            }

            Clock.fpsCount += 1;
            Clock.msCount += delta();
            Clock.time = monotonic();
        } else {
            Clock.initialFPS = fps;
            if (fps > 0) Clock.frameTime = Clock.MS_IN_SEC / fps;
            Clock.time = monotonic();
        }
    }

}
