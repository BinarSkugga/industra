/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.utils;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

// TODO: This class needs some improvements
public class Clock {
    private static long nsInSec = TimeUnit.SECONDS.toNanos(1);
    private static long nsInMillis = TimeUnit.MILLISECONDS.toNanos(1);
    private long time;
    @Getter private int initialFPS = 0;
    private long frameTime = 0;
    @Getter private int fps = 0;
    private int fpsCount = 0;
    private long nsCount = 0;

    public long monotonic() {
        return System.nanoTime();
    }

    public long delta() {
        return this.monotonic() - this.time;
    }

    public void calibrate(int fps) {
        this.initialFPS = fps;
        if (fps > 0) this.frameTime = nsInSec / fps;
    }

    public void tick() {
        this.time = this.monotonic();
    }

    public void tock() {
        try {
            long waitingTime = this.frameTime - this.delta();
            if (waitingTime < 0) waitingTime = 0;

            long ms = waitingTime / nsInMillis;
            int ns = (int) (waitingTime % nsInMillis);
            Thread.sleep(ms, ns);
        } catch (Exception e) {
            Logger.error("Clock thread skipped a frame.");
        }

        if (this.nsCount >= nsInSec) {
            this.fps = this.fpsCount;
            this.fpsCount = 0;
            this.nsCount = this.nsCount - nsInSec;
        }

        this.fpsCount += 1;
        this.nsCount += this.delta();
    }

}
