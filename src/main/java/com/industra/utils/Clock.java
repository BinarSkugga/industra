/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.utils;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

// TODO: This class needs some improvements
public class Clock {
    private static long MS_IN_SEC = TimeUnit.SECONDS.toMillis(1);

    private long time = 0;
    @Getter private int initialFPS = 0;
    private long frameTime = 0;
    @Getter private int fps = 0;
    private int fpsCount = 0;
    private long nsCount = 0;

    public long monotonic() {
        return System.nanoTime() / 1000000;
    }

    public long delta() {
        return this.monotonic() - this.time;
    }

    public void sync(int fps) {
        if(this.time > 0 && this.frameTime > 0) {
            try {
                long waitingTime = this.frameTime - this.delta();
                if (waitingTime < 0) waitingTime = 0;
                TimeUnit.MILLISECONDS.sleep(waitingTime);
            } catch (InterruptedException e) {
                Logger.error("Clock skipped a frame.");
            }

            if (this.nsCount >= MS_IN_SEC) {
                Logger.out(this.fpsCount);
                this.fps = this.fpsCount;
                this.fpsCount = 0;
                this.nsCount = this.nsCount - MS_IN_SEC;
            }

            this.fpsCount += 1;
            this.nsCount += this.delta();
            this.time = this.monotonic();
        } else {
            this.initialFPS = fps;
            if (fps > 0) this.frameTime = MS_IN_SEC / fps;
            this.time = this.monotonic();
        }
    }

}
