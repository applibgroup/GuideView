package com.example.guideview;

import ohos.app.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder class to  construct GuideView object.
 */

public class GuideViewFragmentBuilder {
    private List<GuideViewBundle> guideViewBundles = new ArrayList<>();
    private boolean cancelable;

    /**
     * Adds GuideViewBundle to the list.
     *
     * @param bundle GuideViewBundle object
     * @return GuideViewFragmentBuilder object
     */
    public GuideViewFragmentBuilder addGuideViewBundle(GuideViewBundle bundle) {
        if (bundle == null) {
            return this;
        }
        guideViewBundles.add(bundle);
        return this;
    }

    public GuideViewFragmentBuilder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * Builds the GuideViewFragment object.
     *
     * @param context
     * @return GuideViewFragment object
     */
    public GuideViewFragment build(Context context) {
        GuideViewFragment fragment = new GuideViewFragment(context);
        fragment.setGuideViewBundles(guideViewBundles);
        fragment.siteRemovable(cancelable);
        return fragment;
    }
}
