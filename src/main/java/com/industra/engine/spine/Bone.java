/*
 * Copyright (c) 2020 Charles Smith
 */

package com.industra.engine.spine;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Bone implements Updatable {
    static public boolean yDown;

    @Getter private BoneData data;
    @Getter private Skeleton skeleton;
    @Getter private Bone parent;
    @Getter private List<Bone> children = new ArrayList<>();
    @Getter private boolean active;
    @Getter private boolean sorted;
    @Getter private boolean appliedValid;

    @Getter @Setter private float x;
    @Getter @Setter private float y;
    @Getter @Setter private float rotation;
    @Getter @Setter private float scaleX;
    @Getter @Setter private float scaleY;
    @Getter @Setter private float shearX;
    @Getter @Setter private float shearY;

    @Getter @Setter private float ax;
    @Getter @Setter private float ay;
    @Getter @Setter private float arotation;
    @Getter @Setter private float ascaleX;
    @Getter @Setter private float ascaleY;
    @Getter @Setter private float ashearX;
    @Getter @Setter private float ashearY;

    @Getter private float a;
    @Getter private float b;
    @Getter private float c;
    @Getter private float d;

    @Getter private float worldX;
    @Getter private float worldY;

    public float worldRotationX() {
        return MathUtils.atan2(this.c, this.a) * MathUtils.RAD_DEG;
    }

    public float worldRotationY() {
        return MathUtils.atan2(this.d, this.b) * MathUtils.RAD_DEG;
    }

    public float worldScaleX() {
        return (float) Math.sqrt(a * a + c * c);
    }

    public float worldScaleY() {
        return (float) Math.sqrt(b * b + d * d);
    }

    public Bone(@NonNull BoneData data, @NonNull Skeleton skeleton, Bone parent) {
        this.data = data;
        this.skeleton = skeleton;
        this.parent = parent;
        this.setToSetupPose();
    }

    public void setToSetupPose() {
        BoneData data = this.data;
        x = data.x();
        y = data.y();
        rotation = data.rotation();
        scaleX = data.scaleX();
        scaleY = data.scaleY();
        shearX = data.shearX();
        shearY = data.shearY();
    }

    public void update () {
        UpdateWorldTransform(x, y, rotation, scaleX, scaleY, shearX, shearY);
    }

    public void updateWorldTransform () {
        UpdateWorldTransform(x, y, rotation, scaleX, scaleY, shearX, shearY);
    }

    public void UpdateWorldTransform (float x, float y, float rotation, float scaleX, float scaleY, float shearX, float shearY) {
        ax = x;
        ay = y;
        arotation = rotation;
        ascaleX = scaleX;
        ascaleY = scaleY;
        ashearX = shearX;
        ashearY = shearY;
        appliedValid = true;
        Skeleton skeleton = this.skeleton;

        Bone parent = this.parent;
        if (parent == null) { // Root bone.
            float rotationY = rotation + 90 + shearY, sx = skeleton.scaleX(), sy = skeleton.scaleY();
            a = MathUtils.cosDeg(rotation + shearX) * scaleX * sx;
            b = MathUtils.cosDeg(rotationY) * scaleY * sx;
            c = MathUtils.sinDeg(rotation + shearX) * scaleX * sy;
            d = MathUtils.sinDeg(rotationY) * scaleY * sy;
            worldX = x * sx + skeleton.x();
            worldY = y * sy + skeleton.y();
            return;
        }

        float pa = parent.a, pb = parent.b, pc = parent.c, pd = parent.d;
        worldX = pa * x + pb * y + parent.worldX;
        worldY = pc * x + pd * y + parent.worldY;

        float rotationY;
        float la, lb, lc, ld;
        switch (data.transformMode()) {
            case NORMAL:
                rotationY = rotation + 90 + shearY;
                la = MathUtils.cosDeg(rotation + shearX) * scaleX;
                lb = MathUtils.cosDeg(rotationY) * scaleY;
                lc = MathUtils.sinDeg(rotation + shearX) * scaleX;
                ld = MathUtils.sinDeg(rotationY) * scaleY;
                a = pa * la + pb * lc;
                b = pa * lb + pb * ld;
                c = pc * la + pd * lc;
                d = pc * lb + pd * ld;
                return;
            case ONLY_TRANSITION:
                rotationY = rotation + 90 + shearY;
                a = MathUtils.cosDeg(rotation + shearX) * scaleX;
                b = MathUtils.cosDeg(rotationY) * scaleY;
                c = MathUtils.sinDeg(rotation + shearX) * scaleX;
                d = MathUtils.sinDeg(rotationY) * scaleY;
                break;
            case NO_ROTATION_OR_REFLECTION: {
                    float s = pa * pa + pc * pc, prx;
                    if (s > 0.0001f) {
                        s = Math.abs(pa * pd - pb * pc) / s;
                        pa /= skeleton.scaleX();
                        pc /= skeleton.scaleY();
                        pb = pc * s;
                        pd = pa * s;
                        prx = MathUtils.atan2(pc, pa) * MathUtils.RAD_DEG;
                    } else {
                        pa = 0;
                        pc = 0;
                        prx = 90 - MathUtils.atan2(pd, pb) * MathUtils.RAD_DEG;
                    }
                    float rx = rotation + shearX - prx;
                    float ry = rotation + shearY - prx + 90;
                    la = MathUtils.cosDeg(rx) * scaleX;
                    lb = MathUtils.cosDeg(ry) * scaleY;
                    lc = MathUtils.sinDeg(rx) * scaleX;
                    ld = MathUtils.sinDeg(ry) * scaleY;
                    a = pa * la - pb * lc;
                    b = pa * lb - pb * ld;
                    c = pc * la + pd * lc;
                    d = pc * lb + pd * ld;
                    break;
                }
            case NO_SCALE:
            case NO_SCALE_OR_REFLECTION: {
                    float cos = MathUtils.cosDeg(rotation), sin = MathUtils.sinDeg(rotation);
                    float za = (pa * cos + pb * sin) / skeleton.scaleX();
                    float zc = (pc * cos + pd * sin) / skeleton.scaleY();
                    float s = (float)Math.sqrt(za * za + zc * zc);
                    if (s > 0.00001f) s = 1 / s;
                    za *= s;
                    zc *= s;
                    s = (float)Math.sqrt(za * za + zc * zc);

                    if (data.transformMode() == TransformMode.NO_SCALE &&
                            (pa * pd - pb * pc < 0) != (skeleton.scaleX() < 0 != skeleton.scaleY() < 0))
                        s = -s;

                    float r = MathUtils.PI / 2 + MathUtils.atan2(zc, za);
                    float zb = MathUtils.cos(r) * s;
                    float zd = MathUtils.sin(r) * s;
                    la = MathUtils.cosDeg(shearX) * scaleX;
                    lb = MathUtils.cosDeg(90 + shearY) * scaleY;
                    lc = MathUtils.sinDeg(shearX) * scaleX;
                    ld = MathUtils.sinDeg(90 + shearY) * scaleY;
                    a = za * la + zb * lc;
                    b = za * lb + zb * ld;
                    c = zc * la + zd * lc;
                    d = zc * lb + zd * ld;
                    break;
                }
        }

        a *= skeleton.scaleX();
        b *= skeleton.scaleX();
        c *= skeleton.scaleY();
        d *= skeleton.scaleY();
    }

    private void updateAppliedTransform () {
        appliedValid = true;
        Bone parent = this.parent;
        if (parent == null) {
            ax = worldX;
            ay = worldY;
            arotation = MathUtils.atan2(c, a) * MathUtils.RAD_DEG;
            ascaleX = (float)Math.sqrt(a * a + c * c);
            ascaleY = (float)Math.sqrt(b * b + d * d);
            ashearX = 0;
            ashearY = MathUtils.atan2(a * b + c * d, a * d - b * c) * MathUtils.RAD_DEG;
            return;
        }
        float pa = parent.a, pb = parent.b, pc = parent.c, pd = parent.d;
        float pid = 1 / (pa * pd - pb * pc);
        float dx = worldX - parent.worldX, dy = worldY - parent.worldY;
        ax = (dx * pd * pid - dy * pb * pid);
        ay = (dy * pa * pid - dx * pc * pid);
        float ia = pid * pd;
        float id = pid * pa;
        float ib = pid * pb;
        float ic = pid * pc;
        float ra = ia * a - ib * c;
        float rb = ia * b - ib * d;
        float rc = id * c - ic * a;
        float rd = id * d - ic * b;
        ashearX = 0;
        ascaleX = (float)Math.sqrt(ra * ra + rc * rc);
        if (ascaleX > 0.0001f) {
            float det = ra * rd - rb * rc;
            ascaleY = det / ascaleX;
            ashearY = MathUtils.atan2(ra * rb + rc * rd, det) * MathUtils.RAD_DEG;
            arotation = MathUtils.atan2(rc, ra) * MathUtils.RAD_DEG;
        } else {
            ascaleX = 0;
            ascaleY = (float)Math.sqrt(rb * rb + rd * rd);
            ashearY = 0;
            arotation = 90 - MathUtils.atan2(rd, rb) * MathUtils.RAD_DEG;
        }
    }

    public float[] worldToLocal (float worldX, float worldY) {
        float a = this.a, b = this.b, c = this.c, d = this.d;
        float invDet = 1 / (a * d - b * c);
        float x = worldX - this.worldX, y = worldY - this.worldY;

        float localX = (x * d * invDet - y * b * invDet);
        float localY = (y * a * invDet - x * c * invDet);
        return new float[]{localX, localY};
    }

    public float[] localToWorld (float localX, float localY) {
        float worldX = localX * a + localY * b + this.worldX;
        float worldY = localX * c + localY * d + this.worldY;
        return new float[]{worldX, worldY};
    }

    public float worldToLocalRotationX () {
        Bone parent = this.parent;
        if (parent == null) return arotation;
        float pa = parent.a, pb = parent.b, pc = parent.c, pd = parent.d, b = this.b, d = this.d;
        return MathUtils.atan2(pa * d - pc * b, pd * b - pb * d) * MathUtils.RAD_DEG;
    }

    public float worldToLocalRotation (float worldRotation) {
        float sin = MathUtils.sinDeg(worldRotation), cos = MathUtils.cosDeg(worldRotation);
        return MathUtils.atan2(a * sin - c * cos, d * cos - b * sin) * MathUtils.RAD_DEG + rotation - shearX;
    }

    public float localToWorldRotation (float localRotation) {
        localRotation -= rotation - shearX;
        float sin = MathUtils.sinDeg(localRotation), cos = MathUtils.cosDeg(localRotation);
        return MathUtils.atan2(cos * c + sin * d, cos * a + sin * b) * MathUtils.RAD_DEG;
    }

    public void RotateWorld (float degrees) {
        float a = this.a, b = this.b, c = this.c, d = this.d;
        float cos = MathUtils.cosDeg(degrees), sin = MathUtils.sinDeg(degrees);
        this.a = cos * a - sin * c;
        this.b = cos * b - sin * d;
        this.c = sin * a + cos * c;
        this.d = sin * b + cos * d;
        appliedValid = false;
    }

    @Override
    public String toString() {
        return this.data.name();
    }
}
