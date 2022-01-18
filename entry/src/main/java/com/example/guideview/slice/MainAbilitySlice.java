package com.example.guideview.slice;

import com.example.guideview.GuideViewBundle;
import com.example.guideview.GuideViewFragment;
import com.example.guideview.GuideViewFragmentBuilder;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import com.example.guideview.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

import static com.example.guideview.GuideViewBundle.Direction.*;
import static com.example.guideview.GuideViewBundle.TransparentOutline.TYPE_OVAL;
import static com.example.guideview.GuideViewBundle.TransparentOutline.TYPE_RECT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;

/**
 * Slice for Main Ability.
 */
public class MainAbilitySlice extends AbilitySlice {

    private GuideViewFragment guideViewFragment;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        initUi();
    }

    private void initUi() {
        Component hintViewLeft = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_guideview_left, null, false);
        Component hintViewTop = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_guideview_top, null, false);
        Component hintViewRight = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_guideview_right, null, false);
        Component hintViewBottom = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_guideview_bottom, null, false);

        Component tvContent = findComponentById(ResourceTable.Id_tvContent);
        tvContent.setClickedListener(component ->

                new ToastDialog(MainAbilitySlice.this)
                        .setText("target view is clicked")
                        .show());

        hintViewLeft.findComponentById(ResourceTable.Id_tvLeftNext)
                .setClickedListener(component -> guideViewFragment.onNext());

        hintViewTop.findComponentById(ResourceTable.Id_tvTopNext)
                .setClickedListener(component -> guideViewFragment.onNext());

        hintViewRight.findComponentById(ResourceTable.Id_tvRighttNext)
                .setClickedListener(component -> guideViewFragment.onNext());

        DependentLayout.LayoutConfig configParam = new DependentLayout.LayoutConfig(dpToPx(this, 180f), MATCH_CONTENT);

        int space = dpToPx(this, 10f);

        findComponentById(ResourceTable.Id_tvShow).setClickedListener(component -> {
            guideViewFragment = GuideViewFragmentBuilder.newInstance()
                    .addGuideViewBundle(new GuideViewBundle.Builder()
                            .setTargetView(tvContent)
                            .setHintView(hintViewLeft)
                            .setDismissOnClicked(false)
                            .condition(false)
                            .setHintViewMargin(0, -160, 0, 0)
                            .setTransparentSpace(space, space, space, space)
                            .setOutlineType(TYPE_RECT)
                            .setTargetViewClickable(true)
                            .setHintViewParams(configParam)
                            .setHintViewDirection(LEFT)
                            .build())
                    .addGuideViewBundle(new GuideViewBundle.Builder()
                            .setTargetView(tvContent)
                            .setOutlineType(TYPE_OVAL)
                            .setHintView(hintViewTop)
                            .setDismissOnClicked(false)
                            .setHintViewParams(configParam)
                            .setGuideViewHideListener(() -> showToastDialog("dismissed"))
                            //.setHintViewMargin(50, 0, 0, 0)//
                            .setHintViewMargin(-dpToPx(this, 55f), 0, 0, 0)
                            //.setTransparentSpace(space, space, space, space)
                            .setHintViewDirection(TOP)
                            .build())
                    .addGuideViewBundle(new GuideViewBundle.Builder()
                            .setTargetView(tvContent)
                            .setOutlineType(TYPE_OVAL)
                            .setHintView(hintViewRight)
                            .setDismissOnClicked(false)
                            .setHintViewParams(configParam)
                            .setHintViewMargin(0, -160, 0, 0)
                            .setTransparentSpace(space, space, space, space)
                            .setHintViewDirection(RIGHT)
                            .build())
                    .addGuideViewBundle(new GuideViewBundle.Builder()
                            .setTargetView(tvContent)
                            .setOutlineType(TYPE_OVAL)
                            .setHintViewParams(configParam)
                            .setHintViewMargin(dpToPx(this, 55f), 0, 0, 0)
                            .setHintView(hintViewBottom)
                            .setTransparentSpace(space, space, space, space)
                            .setHintViewDirection(BOTTOM)
                            .build())
                    .setCancelable(false)
                    .build(MainAbilitySlice.this);
            guideViewFragment.show();
        });
    }

    private void showToastDialog(String msg) {
        new ToastDialog(MainAbilitySlice.this)
                .setText(msg)
                .setAlignment(LayoutAlignment.CENTER)
                .setSize(MATCH_CONTENT, MATCH_CONTENT)
                .show();
    }

    /**
     * Converts dp units to pixels.
     *
     * @param context Context
     * @param dp            A distance measurement, in dp.
     * @return              The value of the provided dp units, in pixels.
     */
    public static int dpToPx(Context context, float dp) {
        return (int) (context.getResourceManager().getDeviceCapability().screenDensity / 160 * dp);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
