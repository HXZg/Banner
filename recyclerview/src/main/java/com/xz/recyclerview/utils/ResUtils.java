package com.xz.recyclerview.utils;

import android.content.Context;

public class ResUtils {

    public static int getDimensionPixelSize(Context context, int attr) {
        return context.getResources().getDimensionPixelSize(attr);
    }

    public static int getColor(Context context, int attr) {
        return context.getResources().getColor(attr);
    }
}
