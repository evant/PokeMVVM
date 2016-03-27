package me.tatarka.pokemvvm.pokemon;

import android.animation.Animator;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;

public class SlideDownAndFadeOut extends Visibility {

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null) {
            return null;
        }
        return SlideAndFadeAnimatorUtil.hide(view);
    }
}
