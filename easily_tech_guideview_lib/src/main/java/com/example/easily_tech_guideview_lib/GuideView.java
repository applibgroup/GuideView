package com.example.easily_tech_guideview_lib;

import ohos.aafwk.ability.Ability;
import ohos.agp.components.*;
import ohos.agp.render.*;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.agp.render.Canvas.PorterDuffMode;
import ohos.agp.utils.RectFloat;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;

import static com.example.easily_tech_guideview_lib.GuideViewBundle.Direction.BOTTOM;
import static com.example.easily_tech_guideview_lib.GuideViewBundle.Direction.LEFT;
import static com.example.easily_tech_guideview_lib.GuideViewBundle.Direction.RIGHT;
import static com.example.easily_tech_guideview_lib.GuideViewBundle.Direction.TOP;
import static com.example.easily_tech_guideview_lib.GuideViewBundle.TransparentOutline.TYPE_OVAL;
import static com.example.easily_tech_guideview_lib.GuideViewBundle.TransparentOutline.TYPE_RECT;


@SuppressWarnings("ViewConstructor")
final class GuideView extends DependentLayout {
    interface TargetViewClickListener {
        void onGuideViewClicked();
    }
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

    GuideView (Context context, GuideViewBundle bundle) {
    super(context);
    this.bundle = bundle;
    screenWidth = Component.EstimateSpec.getSize(getEstimatedWidth());
    screenHeight = Component.EstimateSpec.getSize(getEstimatedHeight());
    backgroundPaint = new Paint();
    transparentPaint = new Paint();
    backgroundPaint.setColor(new Color(bundle.getMaskColor()));
    }

  //  @Override
    public boolean dispatchTouchEvent(TouchEvent event) {
        if (bundle.isTargetViewClickAble() && isTouchOnTargetView(event)) {
            if (getContext() instanceof Ability) {
                ((Ability) getContext()).onEventDispatch();
            }
            if (event.getAction() == TouchEvent.PRIMARY_POINT_UP) {
                if (targetViewClickListener != null) {
                    targetViewClickListener.onGuideViewClicked();
                }
            }
        return true;
        }
        return dispatchTouchEvent(event);
}
//   @Override
    protected void onDraw (Canvas canvas) {
        if (bundle == null){
            return;
        }
    drawBackGround(canvas);
    }

    private void drawBackGround(Canvas canvas) {
        PixelMap pixelMap;
        if (screenWidth == 0 || screenHeight == 0) {
            PixelMap.InitializationOptions options1 = new PixelMap.InitializationOptions();
            options1.size = new Size(getWidth(), getHeight());
            options1.pixelFormat = PixelFormat.ARGB_8888;

        } else {
            PixelMap.InitializationOptions options2 = new PixelMap.InitializationOptions();
            options2.size = new Size(getWidth(), getHeight());
            options2.pixelFormat = PixelFormat.ARGB_8888;

        }

        Canvas backGround = new Canvas();
        backGround.drawRect(0, 0, canvas.getLocalClipBounds().getWidth(), canvas.getLocalClipBounds().getHeight(), backgroundPaint);
        canvas.drawColor(Color.TRANSPARENT.getValue(), PorterDuffMode.DST_OUT);
        transparentPaint.setAntiAlias(true);
        if (bundle.isHasTransparentLayer()) {
         //   int extraHeight = BuildConfig.VERSION_CODE < BuildConfig.VERSION_NAME() ? Utils.getStatusBarHeight(getContext()): 0 ;
            float left = targetViewLocation[0] - bundle.getTransparentSpaceLeft();
            float top = targetViewLocation[1] - bundle.getTransparentSpaceTop();
            float right = targetViewLocation[0] + targetViewWidth + bundle.getTransparentSpaceRight();
            float bottom = targetViewLocation[1] + targetViewHeight + bundle.getTransparentSpaceBottom();
            RectFloat rectFloat = new RectFloat(left, top, right, bottom);
            switch (bundle.getOutlineType()) {
                case TYPE_OVAL:
                    backGround.drawOval(rectFloat, transparentPaint);
                    break;
                case TYPE_RECT:
                    backGround.drawRect(rectFloat, transparentPaint);
                    break;
                default:
                    backGround.drawOval(rectFloat, transparentPaint);
            }
        }
    canvas.drawPoint(0 ,0, backgroundPaint);
    }

    private boolean isTouchOnTargetView(TouchEvent event) {
        if (bundle == null || bundle.getTargetView() == null) {
            return false;
        }
        int yAxis = (int) getComponentPosition().getPivotYCoordinate();
        int xAxis = (int) getComponentPosition().getPivotXCoordinate();
        Component targetView = bundle.getTargetView();
        int[] location = new int[2];
        targetView.getLocationOnScreen();
        int left = location[0];
        int top = location[1];
        int right = left + targetView.getWidth();
        int bottom = top + targetView.getHeight();
        if (yAxis >= top && yAxis <= bottom && xAxis >= left && xAxis <= right) {
            return true;
        }
        return false;
    }
    private void addHintView(){
        if(hasAddHintView||bundle.getHintView()==null){
        return;
        }
        StackLayout.LayoutConfig config=bundle.getHintViewParams()==null?new StackLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_CONTENT,ComponentContainer.LayoutConfig.MATCH_CONTENT):bundle.getHintViewParams();
        int left, top, right, bottom;
        left = top = right = bottom = 0;

        int gravity = LayoutAlignment.TOP | LayoutAlignment.START;

        int viewHeight = getHeight();
      //  int extraHeight = BuildConfig.VERSION_CODE < BuildConfig.VERSION_NAME ? Utils.getStatusBarHeight(getContext()): 0;
        switch (bundle.getHintViewDirection()) {
            case LEFT:
                setGravity(LayoutAlignment.END);
                top = targetViewLocation[1] + bundle.getHintViewMarginTop();
                right = screenWidth - targetViewLocation[0] + bundle.getHintViewMarginRight() + bundle.getTransparentSpaceLeft();
                break;
            case RIGHT:
                setGravity(LayoutAlignment.START);
                top = targetViewLocation[1] + bundle.getHintViewMarginTop();
                left = targetViewLocation[0] + targetViewWidth + bundle.getHintViewMarginLeft() + bundle.getTransparentSpaceRight() ;
                break;
            case TOP:
                setGravity(LayoutAlignment.BOTTOM);
                bottom = viewHeight - targetViewLocation[1] + bundle.getHintViewMarginBottom() + bundle.getTransparentSpaceTop();
                left = targetViewLocation[0] + bundle.getHintViewMarginLeft();
                break;
            case BOTTOM:
                setGravity(LayoutAlignment.TOP);
                top = targetViewLocation[1] + targetViewHeight + bundle.getHintViewMarginTop() + bundle.getTransparentSpaceBottom();
                left = targetViewLocation[0] + bundle.getHintViewMarginLeft();
                break;
        }
        setGravity(gravity);
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
        targetView.getLocationOnScreen();
        targetViewWidth = targetView.getWidth();
        targetViewHeight = targetView.getHeight();
        if (targetViewLocation[0] >= 0 && targetViewLocation[1] > 0) {
            return true;
        }
    }
    return false;
}
    public void setTargetViewClickListener (TargetViewClickListener targetViewClickListener) {
    this.targetViewClickListener = targetViewClickListener;
}
    public void show() {
    if (bundle.getTargetView() == null){
        return;
    }
    bundle.getTargetView().postInRenderThread
//            (new Runnable(){
//        @Override
//        public void run() {
//            setShowing();
//        }
//    });
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
    private void hide() {
    this.removeAllComponents();
    if (getComponentParent() != null && bundle.getGuideViewHideListener() != null ){
        bundle.getGuideViewHideListener().onGuideViewHide();
    }
    }
}

