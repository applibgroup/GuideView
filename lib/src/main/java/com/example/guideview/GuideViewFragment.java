package com.example.guideview;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.service.Window;
import ohos.app.Context;
import java.util.ArrayList;
import java.util.List;
/**
 * show the guidView using DialogFragment,it has such features:
 * 1.the guideView can be dismiss,when clicked the back-event key;
 * 2.control the guideView as a series
 *
 * <p>Created by lemon on 2018/4/16.
 */

public class GuideViewFragment extends CommonDialog {
    private static Context context;
    private List<GuideViewBundle> guideViewBundles = new ArrayList<>();
    private StackLayout flContainer;
    private GuideViewBundle currentBundle;
    private GuideView currentGuideView;
    private boolean isShowing;
    private Component layout;

    /**
     * Constructor.
     * @param cxt context instance
     */
    public GuideViewFragment(Context cxt) {
        super(context);
        context = cxt;
        layout = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_layout_guide_container, null, false);

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setTitleText("CommonDialog");
        this.setContentCustomComponent(layout);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setWindowLayout(ComponentContainer.LayoutConfig.MATCH_PARENT,
                ComponentContainer.LayoutConfig.MATCH_PARENT);
        window.setBackgroundColor(new RgbColor(Color.TRANSPARENT.getValue()));
        if (!isShowing) {
            isShowing = true;
            show();
        }
    }

    public void setGuideViewBundles(List<GuideViewBundle> guideViewBundles) {
        if (guideViewBundles == null || guideViewBundles.isEmpty()) {
            return;
        }
        this.guideViewBundles = guideViewBundles;
    }

    /**
     * when the guideView is clicked (or the item in guideView is clicked for next),this method need to be called
     * if the GuideView is allowed to dismissOnClicked
     * (by setting easily.tech.guideview.lib.GuideViewBundle.Builder#setDismissOnClicked(boolean)}),
     * this method will be called automatically
     * otherwise,you need to handle it yourself.
     */
    public void onNext() {
        showGuideView();
    }

    /**
     * whether there is another guiView to show on next click.
     *
     * @return boolean True or False
     */
    public boolean hasNext() {
        return guideViewBundles != null && !guideViewBundles.isEmpty();
    }

    private void showGuideView() {
        // remove the current guideView before showing next one
        if (currentGuideView != null && currentGuideView.isShowing) {
            // set the container background as the mask color,when the next guideView show,it will reset to transparent
            // in order to keep reduce the blinking in the interval
            ShapeElement shapeElement = new ShapeElement();
            if (currentBundle == null) {
                shapeElement.setRgbColor(new RgbColor(ohos.agp.utils.Color.TRANSPARENT.getValue()));
            } else {
                shapeElement.setRgbColor(RgbColor.fromArgbInt(currentBundle.getMaskColor()));
            }
            flContainer.setBackground(shapeElement);
            currentGuideView.hide();
        }
        // loop to get the available guideView bundle data
        do {
            if (guideViewBundles == null || guideViewBundles.isEmpty()) {
                currentBundle = null;
            } else {
                currentBundle = guideViewBundles.remove(0);
            }
        } while (currentBundle != null && !currentBundle.condition());
        if (currentBundle == null) {
            hide();
            return;
        }
        if(null!=currentGuideView) {
            GuideView guideView = new GuideView((currentGuideView.getContext()), currentBundle);
            wrapClickListener(guideView);
            guideView.setTargetViewClickListener(() -> {
                if (currentBundle != null && currentBundle.isDismissOnTouchInTargetView()) {
                    onNext();
                }
            });
            flContainer.addComponent(guideView);
            guideView.show();
            currentGuideView = guideView;
        }
    }

    private void wrapClickListener(Component guideView) {
        if (currentBundle == null || !currentBundle.isDismissOnClicked()) {
            return;
        }
        guideView.setClickedListener(component -> onNext());
    }

    @Override
    public void hide() {
        if (getButtonComponent().getContext() instanceof CommonDialog
                && ((CommonDialog) getButtonComponent().getContext()).isShowing()) {
            if (flContainer != null) {
                flContainer.removeAllComponents();
                currentBundle = null;
                currentGuideView = null;
            }
            isShowing = false;
            super.onWindowSelectionUpdated(false);
        }
    }

    public static class Builder {
        private List<GuideViewBundle> guideViewBundles = new ArrayList<>();
        private boolean cancelable;

        public Builder addGuideViewBundle(GuideViewBundle bundle) {
            if (bundle == null) {
                return this;
            }
            guideViewBundles.add(bundle);
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public GuideViewFragment build() {
            GuideViewFragment fragment = new GuideViewFragment(context);
            fragment.setGuideViewBundles(guideViewBundles);
            fragment.siteRemovable(cancelable);
            return fragment;
        }
    }
}
