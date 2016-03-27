package me.tatarka.pokemvvm.pokemon;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import me.tatarka.pokemvvm.R;

public class ChipLayout extends LinearLayout {

    public ChipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        if (isInEditMode()) {
            setChips(Arrays.asList(
                    new Chip(getResources().getColor(R.color.green), "grass"),
                    new Chip(getResources().getColor(R.color.purple), "poison")
            ));
        }
    }

    public void setChips(Collection<Chip> chips) {
        removeAllViews();
        if (chips.isEmpty()) {
            return;
        }
        {
            Space startSpace = new Space(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            addView(startSpace, params);
        }
        for (Iterator<Chip> iterator = chips.iterator(); iterator.hasNext(); ) {
            Chip chip = iterator.next();
            TextView chipView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.pokemon_chip, this, false);
            chipView.setText(chip.text());
            ViewCompat.setBackgroundTintList(chipView, ColorStateList.valueOf(chip.color()));
            ViewCompat.setBackgroundTintMode(chipView, PorterDuff.Mode.MULTIPLY);
            addView(chipView);
            if (iterator.hasNext()) {
                Space space = new Space(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.padding_normal), ViewGroup.LayoutParams.MATCH_PARENT);
                addView(space, params);
            }
        }

        if (ViewCompat.isLaidOut(this)) {
            setTranslationX(getWidth());
            animate().translationX(0)
                    .setStartDelay(200)
                    .setDuration(600)
                    .setInterpolator(new OvershootInterpolator(0.8f));
        }
    }
}
