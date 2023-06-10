package com.example.myapplication;

import android.view.View;

public class DoubleClickListener implements View.OnClickListener {
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Time between two clicks in milliseconds
    private long lastClickTime = 0;
    private final OnDoubleClickListener onDoubleClickListener;

    public DoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    @Override
    public void onClick(View view) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            // Double-click detected
            onDoubleClickListener.onDoubleClick(view);
        }
        lastClickTime = clickTime;
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(View view);
    }
}