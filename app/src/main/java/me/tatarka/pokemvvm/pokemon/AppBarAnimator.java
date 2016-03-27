package me.tatarka.pokemvvm.pokemon;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

public class AppBarAnimator {

    public static void animateIn(final AppBarLayout appBar) {
        TypedValue value = new TypedValue();
        Context context = appBar.getContext();
        context.getTheme().resolveAttribute(R.attr.actionBarSize, value, true);
        final int startSize = TypedValue.complexToDimensionPixelSize(value.data, context.getResources().getDisplayMetrics());

        for (int i = 0; i < appBar.getChildCount(); i++) {
            View child = appBar.getChildAt(i);
            child.setAlpha(0);
        }

        if (appBar.getHeight() == 0) {
            appBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    appBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int targetSize = appBar.getHeight();
                    doAnimateIn(appBar, startSize, targetSize);
                }
            });
        } else {
            int targetSize = appBar.getHeight();
            doAnimateIn(appBar, startSize, targetSize);
        }
    }

    private static void doAnimateIn(final AppBarLayout appBar, int startSize, int targetSize) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator appBarHeight = ValueAnimator.ofInt(startSize, targetSize);
        appBarHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = appBar.getLayoutParams();
                params.height = (int) animation.getAnimatedValue();
                appBar.requestLayout();
            }
        });
        set.playTogether(appBarHeight);
        for (int i = 0; i < appBar.getChildCount(); i++) {
            View child = appBar.getChildAt(i);
            ObjectAnimator childAlpha = ObjectAnimator.ofFloat(child, View.ALPHA, 0, 1);
            set.playTogether(childAlpha);
        }
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }
}
