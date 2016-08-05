package com.nodrex.android.tools;

import android.view.View;

/**
 * Created by nchum on 8/5/2016.
 */
public class UiUtil {

    public static void hide(View view){
        if(view == null) return;
        view.setVisibility(View.GONE);
    }

    public static void show(View view){
        if(view == null) return;
        view.setVisibility(View.VISIBLE);
    }

    public static void enable(View view){
        if(view == null) return;
        view.setEnabled(true);
    }

    public static void disable(View view){
        if(view == null) return;
        view.setEnabled(false);
    }

}
