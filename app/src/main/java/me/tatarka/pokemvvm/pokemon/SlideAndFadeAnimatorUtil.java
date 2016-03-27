package me.tatarka.pokemvvm.pokemon;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import me.tatarka.pokemvvm.R;

public class SlideAndFadeAnimatorUtil {

    public static Animator show(View view) {
        int translationAmount = view.getResources().getDimensionPixelOffset(R.dimen.anim_translation);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, translationAmount, 0);
        set.playTogether(alpha, translationY);
        return set;
    }

    public static Animator hide(View view) {
        int translationAmount = view.getResources().getDimensionPixelOffset(R.dimen.anim_translation);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, translationAmount);
        set.playTogether(alpha, translationY);
        return set;
    }
}
