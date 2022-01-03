package com.example.easily_tech_guideview_lib;

import ohos.aafwk.ability.Ability;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.agp.colors.*;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.bundle.BundleInfo;
import ohos.utils.PacMap;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.window.service.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.components.element.ShapeElement;

import java.util.ArrayList;
import java.util.List;

public class GuideViewFragment extends  CommonDialog {
    private List<GuideViewBundle> guideViewBundles = new ArrayList<>();
    private StackLayout flContainer;
    private GuideViewBundle currentBundle;
    private GuideView currentGuideView;
    private boolean isShowing;

    public GuideViewFragment(Context context) {
        super(context);
    }

    //@Override
    public void onCreate(@Nullable BundleInfo savedInstanceState) {
        super.onCreate();
        setTitleSubText(String );
    }
    @Nullable
    //@Override
    public Component onCreateView (@NotNull LayoutScatter scatter, @Nullable ComponentContainer container, @Nullable PacMap savedInstanceState) {
    flContainer = (StackLayout) scatter ;
}

    //@Override
    public void onStart() {
        super.onCreate();
        CommonDialog commonDialog = new CommonDialog(currentGuideView.getContext());
        commonDialog.setTitleText("CommonDialog");
        Window window = commonDialog == null ? null : commonDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setWindowLayout(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
        window.setBackgroundColor(new Color(Color.TRANSPARENT));
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

    public void onNext() {
        show();
    }
    public boolean hasNext() {
        return guideViewBundles != null && !guideViewBundles.isEmpty();
    }

    private void showGuideView() {
        if (currentGuideView != null && currentGuideView.isShowing) {
            flContainer.setBackground(ShapeElement.);
        ShapeElement shapeElement = new ShapeElement();
        if (currentBundle == null) {
            shapeElement.setRgbColor(new RgbColor(Color.TRANSPARENT));
        }
        else {
            shapeElement.setRgbColor(RgbColor.fromArgbInt(currentBundle.getMaskColor()));
        }
        }
            currentGuideView.setVisibility(2);

        do {
            if (guideViewBundles == null || guideViewBundles.isEmpty()) {
                currentBundle = null;
            } else {
                currentBundle = guideViewBundles.remove(0);
            }
        }
        while (currentBundle != null && !currentBundle.condition());
        if (currentBundle == null) {
            dismiss();
            return;
        }
        GuideView guideView = new GuideView((currentGuideView.getContext()), currentBundle);
        guideView.setTargetViewClickListener(new GuideView.TargetViewClickListener(){
            @Override
            public void onGuideViewClicked() {
                if (currentBundle != null && currentBundle.isDismissOnTouchInTargetView()) {
                    onNext();
                }
            }
        });
        flContainer.addComponent(guideView);
        guideView.show();
        currentGuideView = guideView;
    }

    private void wrapClickListener(Component guideView) {
        if (currentBundle == null || !currentBundle.isDismissOnClicked()) {
            return;
        }
        guideView.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                onNext();
            }
        });
    }

    @Override
    public void dismiss() {
        if (getButtonComponent().getContext() instanceof Ability && !((Ability) getButtonComponent().getContext()).isTerminating() && get != null && getDialog) {
        if (flContainer != null) {
            flContainer.removeAllComponents();
            currentBundle = null;
            currentGuideView = null;
        }
        isShowing = false;
        super.onWindowSelectionUpdated(false);
        }
}
    public  static class  Builder {
    private  List<GuideViewBundle> guideViewBundles = new ArrayList<>();
    private boolean cancelable;

        public Builder addGuideViewBundle(GuideViewBundle bundle) {
        if (bundle == null) {
            return this;
        }
        guideViewBundles.add(bundle);
        return this;
        }

        public Builder setCancelable(boolean Cancelable)  {
            this.cancelable = cancelable;
            return this;
        }

        public GuideViewFragment build() {
            GuideViewFragment fragment = new GuideViewFragment();
            fragment.setGuideViewBundles(guideViewBundles);
            fragment.siteRemovable(cancelable);
            return fragment;
            }
        }
    }
