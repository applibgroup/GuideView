package com.example.guideview;

import ohos.aafwk.ability.Ability;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentParent;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.StackLayout;
import ohos.agp.render.Canvas;
import ohos.agp.render.Canvas.PorterDuffMode;
import ohos.agp.render.Paint;
import ohos.agp.render.Texture;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;


@SuppressWarnings("ViewConstructor")
final class GuideView extends DependentLayout implements Component.DrawTask, Component.TouchEventListener {

    private boolean hasAddHintView = false;
    public boolean isShowing = false;
    private int[] targetViewLocation = new int[2];
    private int targetViewWidth;
    private int targetViewHeight;
    private int screenWidth;
    private int screenHeight;
    private Paint backgroundPaint;
    private Paint transparentPaint;
    private GuideViewBundle bundle;
    private TargetViewClickListener targetViewClickListener;
    private EventHandler mHandler;

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (bundle.isTargetViewClickAble() && isTouchOnTargetView()) {
            if (getContext() instanceof Ability) {
                ((Ability) getContext()).onEventDispatch();
            }
            if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_UP) {
                if (targetViewClickListener != null) {
                    targetViewClickListener.onGuideViewClicked();
                }
            }
            return true;
        }
        return false;
    }

    private interface TargetViewClickListener {
        void onGuideViewClicked();
    }


    public GuideView(Context context, GuideViewBundle bundle) {
        super(context);
        this.bundle = bundle;
        screenWidth = Component.EstimateSpec.getSize(getEstimatedWidth());
        screenHeight = Component.EstimateSpec.getSize(getEstimatedHeight());
        backgroundPaint = new Paint();
        transparentPaint = new Paint();
        backgroundPaint.setColor(new Color(this.bundle.getMaskColor()));

        EventRunner runner = EventRunner.getMainEventRunner();
        mHandler = new EventHandler(runner) {
            @Override
            protected void processEvent(InnerEvent event) {
                super.processEvent(event);
                setShowing(); //Will have to check if event is required or not
            }
        };
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (bundle == null) {
            return;
        }
        drawBackGround(canvas);
    }

    private void drawBackGround(Canvas canvas) {
        Rect rectLocalClip = canvas.getLocalClipBounds();
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
        if (rectLocalClip.getWidth() == 0 || rectLocalClip.getHeight() == 0) {
            initializationOptions.size = new Size(screenWidth, screenHeight);
        } else {
            initializationOptions.size = new Size(rectLocalClip.getWidth(),
                    rectLocalClip.getHeight());
        }
        PixelMap pixelMap = PixelMap.create(initializationOptions);
        Canvas backGround = new Canvas(new Texture(pixelMap));
        backGround.drawRect(0, 0, rectLocalClip.getWidth(),
                rectLocalClip.getHeight(), backgroundPaint);
        canvas.drawColor(Color.TRANSPARENT.getValue(), PorterDuffMode.DST_OUT);
        transparentPaint.setAntiAlias(true);
        if (bundle.isHasTransparentLayer()) {
            float left = targetViewLocation[0] - bundle.getTransparentSpaceLeft();
            float top = targetViewLocation[1] - bundle.getTransparentSpaceTop();
            float right = targetViewLocation[0] + targetViewWidth + bundle.getTransparentSpaceRight();
            float bottom = targetViewLocation[1] + targetViewHeight + bundle.getTransparentSpaceBottom();
            RectFloat rectFloat = new RectFloat(left, top, right, bottom);
            if (bundle.getOutlineType() == GuideViewBundle.TransparentOutline.TYPE_RECT) {
                backGround.drawRect(rectFloat, transparentPaint);
            } else {
                backGround.drawOval(rectFloat, transparentPaint);
            }
        }
        canvas.drawPoint(0, 0, backgroundPaint);
    }

    private boolean isTouchOnTargetView() {
        if (bundle == null || bundle.getTargetView() == null) {
            return false;
        }
        int yaxis = getComponentPosition().getPivotYCoordinate();
        int xaxis = getComponentPosition().getPivotXCoordinate();
        Component targetView = bundle.getTargetView();
        float[] location = targetView.getContentPosition();
        int left = (int) location[0];
        int top = (int) location[1];
        int right = left + targetView.getWidth();
        int bottom = top + targetView.getHeight();
        return yaxis >= top && yaxis <= bottom && xaxis >= left && xaxis <= right;
    }

    private void addHintView() {
        if (hasAddHintView || bundle.getHintView() == null) {
            return;
        }

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        switch (bundle.getHintViewDirection()) {
            case GuideViewBundle.Direction.LEFT:
                setGravity(LayoutAlignment.END);
                top = targetViewLocation[1] + bundle.getHintViewMarginTop();
                right = screenWidth - targetViewLocation[0] + bundle.getHintViewMarginRight()
                        + bundle.getTransparentSpaceLeft();
                break;
            case GuideViewBundle.Direction.RIGHT:
                setGravity(LayoutAlignment.START);
                top = targetViewLocation[1] + bundle.getHintViewMarginTop();
                left = targetViewLocation[0] + targetViewWidth + bundle.getHintViewMarginLeft()
                        + bundle.getTransparentSpaceRight();
                break;
            case GuideViewBundle.Direction.TOP:
                setGravity(LayoutAlignment.BOTTOM);
                bottom = getHeight() - targetViewLocation[1] + bundle.getHintViewMarginBottom()
                        + bundle.getTransparentSpaceTop();
                left = targetViewLocation[0] + bundle.getHintViewMarginLeft();
                break;
            case GuideViewBundle.Direction.BOTTOM:
            default:
                setGravity(LayoutAlignment.TOP);
                top = targetViewLocation[1] + targetViewHeight + bundle.getHintViewMarginTop()
                        + bundle.getTransparentSpaceBottom();
                left = targetViewLocation[0] + bundle.getHintViewMarginLeft();
                break;

        }
        setGravity(LayoutAlignment.TOP | LayoutAlignment.START);
        StackLayout.LayoutConfig config = bundle.getHintViewParams() == null
                ? new StackLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_CONTENT,
                ComponentContainer.LayoutConfig.MATCH_CONTENT) : bundle.getHintViewParams();
        config.setMargins(left, top, right, bottom);

        if (bundle.getHintView().getComponentParent() != null) {
            bundle.getHintView().setLayoutConfig(config);
        } else {
            this.addComponent(bundle.getHintView(), config);
        }
        hasAddHintView = true;
    }

    private boolean getTargetViewPosition() {
        Component targetView = bundle.getTargetView();
        if (targetView.getWidth() > 0 && targetView.getHeight() > 0) {
            targetViewLocation = targetView.getLocationOnScreen();
            targetViewWidth = targetView.getWidth();
            targetViewHeight = targetView.getHeight();
            return targetViewLocation[0] >= 0 && targetViewLocation[1] > 0;
        }
        return false;
    }

    public void setTargetViewClickListener(TargetViewClickListener targetViewClickListener) {
        this.targetViewClickListener = targetViewClickListener;
    }

    public void show() {
        if (bundle.getTargetView() == null) {
            return;
        }
        mHandler.sendEvent(InnerEvent.get());
    }

    private void setShowing() {
        boolean hasMeasure = getTargetViewPosition();
        if (isShowing || !hasMeasure) {
            return;
        }
        addHintView();
        this.setScrollbarBackgroundColor(Color.TRANSPARENT);
        if (getComponentParent() != null && getComponentParent() instanceof Component) {

            ((Component) getComponentParent()).setScrollbarBackgroundColor(Color.TRANSPARENT);
        }
        isShowing = true;
    }

    public void hide() {
        this.removeAllComponents();
        if (getComponentParent() != null && getComponentParent() instanceof ComponentParent) {
            getComponentParent().removeComponent(this);
        }
        if (bundle != null && bundle.getGuideViewHideListener() != null) {
            bundle.getGuideViewHideListener().onGuideViewHide();
        }

    }
}

