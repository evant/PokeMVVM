package me.tatarka.pokemvvm.pokemon;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.IdRes;
import android.transition.SidePropagation;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Slides a view out while matching it's y position to the appbar is it animates.
 */
public class AnchorAppBarSlide extends Visibility {
    private static final String PROP_APPBAR_HEIGHT = "me.tatarka.pokemvvm:AnchorAppBarSlide:height";

    @IdRes
    private final int appBarId;

    public AnchorAppBarSlide(@IdRes int appBarId) {
        this.appBarId = appBarId;
        SidePropagation propagation = new SidePropagation();
        propagation.setSide(Gravity.END);
        setPropagation(propagation);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        View appBarLayout = transitionValues.view.getRootView().findViewById(appBarId);
        transitionValues.values.put(PROP_APPBAR_HEIGHT, appBarLayout.getHeight());
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null) {
            return null;
        }
        final int startHeight = (int) startValues.values.get(PROP_APPBAR_HEIGHT);
        final View appBarLayout = sceneRoot.findViewById(appBarId);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, view.getWidth());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (appBarLayout != null) {
                    int currentHeight = appBarLayout.getHeight();
                    view.setTranslationY(currentHeight - startHeight);
                }
            }
        });
        return animator;
    }
}
