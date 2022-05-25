package ru.moskovka.funumber.util;

import android.view.ViewGroup;

public class UIUtils {
    public static void switchVisibility(ViewGroup layout){
        if (layout.getVisibility() == ViewGroup.GONE)
            layout.setVisibility(ViewGroup.VISIBLE);
        else
            layout.setVisibility(ViewGroup.GONE);
    }
}
