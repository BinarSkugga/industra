/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

public abstract class CurveTimeline implements Timeline {
    protected final int LINEAR = 0, STEPPED = 1, BEZIER = 2;
    protected final int BEZIER_SIZE = 10 * 2 - 1;

    private float[] curves;

    public CurveTimeline(int frameCount) {
        if (frameCount <= 0) throw new RuntimeException("frameCount must be > 0: ");
        this.curves = new float[(frameCount - 1) * BEZIER_SIZE];
    }

    public int frameCount() {
        return (this.curves.length / BEZIER_SIZE) + 1;
    }

    public void setLinear(int index) {
        this.curves[index * BEZIER_SIZE] = LINEAR;
    }

    public void setStepped(int index) {
        this.curves[index * BEZIER_SIZE] = STEPPED;
    }

    public int getCurveType(int index) {
        int i = index * BEZIER_SIZE;
        if (i == curves.length) return LINEAR;
        float type = curves[i];
        if (type == LINEAR) return LINEAR;
        if (type == STEPPED) return STEPPED;
        return BEZIER;
    }

    public void setCurve (int frameIndex, float cx1, float cy1, float cx2, float cy2) {
        float tmpx = (-cx1 * 2 + cx2) * 0.03f, tmpy = (-cy1 * 2 + cy2) * 0.03f;
        float dddfx = ((cx1 - cx2) * 3 + 1) * 0.006f, dddfy = ((cy1 - cy2) * 3 + 1) * 0.006f;
        float ddfx = tmpx * 2 + dddfx, ddfy = tmpy * 2 + dddfy;
        float dfx = cx1 * 0.3f + tmpx + dddfx * 0.16666667f, dfy = cy1 * 0.3f + tmpy + dddfy * 0.16666667f;

        int i = frameIndex * BEZIER_SIZE;
        float[] curves = this.curves;
        curves[i++] = BEZIER;

        float x = dfx, y = dfy;
        for (int n = i + BEZIER_SIZE - 1; i < n; i += 2) {
            curves[i] = x;
            curves[i + 1] = y;
            dfx += ddfx;
            dfy += ddfy;
            ddfx += dddfx;
            ddfy += dddfy;
            x += dfx;
            y += dfy;
        }
    }

    public float getCurvePercent (int frameIndex, float percent) {
        percent = MathUtils.clamp(percent, 0, 1);
        float[] curves = this.curves;
        int i = frameIndex * BEZIER_SIZE;
        float type = curves[i];
        if (type == LINEAR) return percent;
        if (type == STEPPED) return 0;
        i++;
        float x = 0;
        for (int start = i, n = i + BEZIER_SIZE - 1; i < n; i += 2) {
            x = curves[i];
            if (x >= percent) {
                if (i == start) return curves[i + 1] * percent / x; // First point is 0,0.
                float prevX = curves[i - 2], prevY = curves[i - 1];
                return prevY + (curves[i + 1] - prevY) * (percent - prevX) / (x - prevX);
            }
        }
        float y = curves[i - 1];
        return y + (1 - y) * (percent - x) / (1 - x); // Last point is 1,1.
    }
}
