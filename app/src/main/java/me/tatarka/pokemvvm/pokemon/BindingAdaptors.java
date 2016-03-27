package me.tatarka.pokemvvm.pokemon;

import android.databinding.BindingAdapter;
import android.view.View;

public class BindingAdaptors {

    @BindingAdapter({"android:visibility", "slideAndFade"})
    public static void setVisibleAnimated(final View view, boolean visible, boolean slideAndFade) {
        if (!slideAndFade) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            return;
        }
        int currentVisibility = view.getVisibility();
        if (currentVisibility == (visible ? View.VISIBLE : View.GONE)) {
            return;
        }
        if (visible) {
            view.setVisibility(View.VISIBLE);
            SlideAndFadeAnimatorUtil.show(view).start();
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
