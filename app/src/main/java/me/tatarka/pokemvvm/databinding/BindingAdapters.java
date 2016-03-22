package me.tatarka.pokemvvm.databinding;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * This class is only for <em>general</em> binding adapters. If you have some specific to some part
 * of the app it should go in it's respective package.
 */
public class BindingAdapters {

    @BindingAdapter("android:visibility")
    public static void setVisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
