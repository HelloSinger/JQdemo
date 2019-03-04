//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jq.btc.utils;

public class GlideOptions {
    private int radius;
    private int replaceImage;
    private int errImage;
    private CornerType cornerType;

    public int getRadius() {
        return radius;
    }

    public int getReplaceImage() {
        return replaceImage;
    }

    public int getErrImage() {
        return errImage;
    }

    public CornerType getCornerType() {
        return cornerType;
    }

    private GlideOptions(Builder builder) {
        radius = builder.radius;
        replaceImage = builder.replaceImage;
        errImage = builder.errImage;
        cornerType = builder.cornerType;
    }

    public enum CornerType {
        ALL,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        TOP, BOTTOM, LEFT, RIGHT,
        TOP_LEFT_BOTTOM_RIGHT,
        TOP_RIGHT_BOTTOM_LEFT,
        TOP_LEFT_TOP_RIGHT_BOTTOM_RIGHT,
        TOP_RIGHT_BOTTOM_RIGHT_BOTTOM_LEFT,
    }


    public static final class Builder {
        private int radius;
        private int replaceImage;
        private int errImage;
        private CornerType cornerType;

        public Builder() {
        }

        public Builder radius(int val) {
            radius = val;
            return this;
        }

        public Builder replaceImage(int val) {
            replaceImage = val;
            return this;
        }

        public Builder errImage(int val) {
            errImage = val;
            return this;
        }

        public Builder cornerType(CornerType val) {
            cornerType = val;
            return this;
        }

        public GlideOptions build() {
            return new GlideOptions(this);
        }
    }
}
