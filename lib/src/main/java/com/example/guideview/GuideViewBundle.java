package com.example.guideview;

import ohos.agp.components.Component;
import ohos.agp.components.StackLayout;
import org.jetbrains.annotations.NotNull;

/**
 * The configuration item class of the guide view,
 * each page of the guide view corresponds to a configuration item.
 */
public class GuideViewBundle {

    /**
     * direction between the hintView and targetView.
     */
    public static final class Direction {
        /**
         * the hintView will align left to the targetView,which top align with it meanwhile.
         */
        public static final int LEFT = 0x0001;
        /**
         * the hintView will align right to the targetView,which top align with it meanwhile.
         */
        public static final int RIGHT = 0x0002;
        /**
         * the hintView will align top to the targetView,which left align with it meanwhile.
         */
        public static final int TOP = 0x0003;
        /**
         * the hintView will align bottom to the targetView,which left align with it meanwhile.
         */
        public static final int BOTTOM = 0x0004;
    }

    /**
     * Interface to support setting the monitoring of GuideView
     * (that is, the guide view of each page) closed.
     */
    public interface GuideViewHideListener {
        void onGuideViewHide();
    }

    /**
     * Transparent focus area outline type.
     */
    public static final class TransparentOutline {
        public static final int TYPE_OVAL = 0;
        public static final int TYPE_RECT = 1;
    }

    private GuideViewBundle.Builder config;

    private GuideViewBundle(@NotNull GuideViewBundle.Builder config) {
        this.config = config;
    }

    public Component getTargetView() {
        return config.targetView;
    }

    public Component getHintView() {
        return config.hintView;
    }

    public int getTransparentSpaceLeft() {
        return config.transparentSpaceLeft;
    }

    public int getTransparentSpaceRight() {
        return config.transparentSpaceRight;
    }

    public int getTransparentSpaceTop() {
        return config.transparentSpaceTop;
    }

    public int getTransparentSpaceBottom() {
        return config.transparentSpaceBottom;
    }

    public int getHintViewMarginLeft() {
        return config.hintViewMarginLeft;
    }

    public int getHintViewMarginRight() {
        return config.hintViewMarginRight;
    }

    public int getHintViewMarginTop() {
        return config.hintViewMarginTop;
    }

    public int getHintViewMarginBottom() {
        return config.hintViewMarginBottom;
    }

    public StackLayout.LayoutConfig getHintViewParams() {
        return config.hintViewParams;
    }

    public int getMaskColor() {
        return config.maskColor;
    }

    public boolean isHasTransparentLayer() {
        return config.hasTransparentLayer;
    }

    public int getHintViewDirection() {
        return config.hintViewDirection;
    }

    public int getOutlineType() {
        return config.outlineType;
    }

    public boolean isDismissOnClicked() {
        return config.isDismissOnClicked;
    }

    public boolean isTargetViewClickAble() {
        return config.isTargetViewClickable;
    }

    public boolean isDismissOnTouchInTargetView() {
        return config.isDismissOnClickTargetView;
    }

    public boolean condition() {
        return config.condition;
    }

    public GuideViewHideListener getGuideViewHideListener() {
        return config.guideViewHideListener;
    }

    /**
     * Builder class to  construct GuideView object.
     */

    public static class Builder {
        private static final int MASK_LAYER_COLOR = 0xd9000000;
        // the guide hint view will align to
        private Component targetView;
        // the assist guidView
        private Component hintView;
        // default,the transparent area is fix the size of the targetView,if you need more space ,set this
        private int transparentSpaceLeft;
        private int transparentSpaceRight;
        private int transparentSpaceTop;
        private int transparentSpaceBottom;
        // default,the hint view is align the targetView based on the Direction
        private int hintViewMarginLeft;
        private int hintViewMarginRight;
        private int hintViewMarginTop;
        private int hintViewMarginBottom;
        // default,the hint view is align the targetView based on the Direction
        private StackLayout.LayoutConfig hintViewParams;
        private boolean hasTransparentLayer = true;
        // whether click the whole screen can dismissed the guideView.
        // If false,you need to handle the click and dismiss event yourself
        private boolean isDismissOnClicked = true;
        // set a condition,whether the added GuideViewBundle can be shown,default is true
        private boolean condition = true;
        private GuideViewHideListener guideViewHideListener;
        private boolean isDismissOnClickTargetView = true;
        private boolean isTargetViewClickable;
        private int hintViewDirection;
        private int outlineType = GuideViewBundle.TransparentOutline.TYPE_OVAL;
        private int maskColor = MASK_LAYER_COLOR;

        public Builder setTargetView(Component targetView) {
            this.targetView = targetView;
            return this;
        }

        public Builder setHintView(Component hintView) {
            this.hintView = hintView;
            return this;
        }

        /**
         * Method to set the alignment of the targetView.
         *
         * @param left left alignment
         * @param top top alignment
         * @param right right alignment
         * @param bottom bottom alignment
         * @return Builder object
         */
        public Builder setTransparentSpace(int left, int top, int right, int bottom) {
            this.transparentSpaceLeft = left;
            this.transparentSpaceTop = top;
            this.transparentSpaceRight = right;
            this.transparentSpaceBottom = bottom;
            return this;
        }

        /**
         * Method to set transparent space around the HintView.
         *
         * @param left left space
         * @param top top space
         * @param right right space
         * @param bottom bottom space
         * @return Builder object
         */
        public Builder setHintViewMargin(int left, int top, int right, int bottom) {
            this.hintViewMarginLeft = left;
            this.hintViewMarginTop = top;
            this.hintViewMarginRight = right;
            this.hintViewMarginBottom = bottom;
            return this;
        }

        public Builder setHintViewParams(StackLayout.LayoutConfig hintViewParams) {
            this.hintViewParams = hintViewParams;
            return this;
        }

        public Builder setHasTransparentLayer(boolean hasTransparentLayer) {
            this.hasTransparentLayer = hasTransparentLayer;
            return this;
        }

        public Builder setDismissOnClicked(boolean dismissOnClicked) {
            isDismissOnClicked = dismissOnClicked;
            return this;
        }

        public Builder setHintViewDirection(int hintViewDirection) {
            this.hintViewDirection = hintViewDirection;
            return this;
        }

        public Builder setOutlineType(int outlineType) {
            this.outlineType = outlineType;
            return this;
        }

        public Builder setMaskColor(int maskColor) {
            this.maskColor = maskColor;
            return this;
        }

        public Builder setTargetViewClickable(boolean targetViewClickable) {
            isTargetViewClickable = targetViewClickable;
            return this;
        }

        public Builder setDismissOnTouchInTargetView(boolean dismissOnTouchInTargetView) {
            isDismissOnClickTargetView = dismissOnTouchInTargetView;
            return this;
        }

        public Builder condition(boolean condition) {
            this.condition = condition;
            return this;
        }

        public Builder setGuideViewHideListener(GuideViewHideListener guideViewHideListener) {
            this.guideViewHideListener = guideViewHideListener;
            return this;
        }

        public GuideViewBundle build() {
            return new GuideViewBundle(this);
        }
    }
}



