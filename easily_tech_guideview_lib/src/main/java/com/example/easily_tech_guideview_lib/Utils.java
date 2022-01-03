package com.example.easily_tech_guideview_lib;

import ohos.app.Context;

public class Utils {

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResourceManager().getIdentifier();
        return context.getResourceManager().getDeviceCapability().height;
        return context.getResourceManager().getDeviceCapability().width;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResourceManager().getDeviceCapability().screenDensity;
        return (int) (dpValue * scale + 0.5f);
    }
}