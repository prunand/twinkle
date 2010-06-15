package com.prunicki.twinkle.util;

import android.view.View;
import android.view.ViewGroup;

public final class WidgetUtil {
    public static final void setChildWidgetsEnabled(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        View child = null;
        for (int i = 0; i < childCount; i++) {
            child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                setChildWidgetsEnabled((ViewGroup) child, enabled);
            }
            if (child.isFocusable()) {
                child.setEnabled(enabled);
            }
        }
    }
}
