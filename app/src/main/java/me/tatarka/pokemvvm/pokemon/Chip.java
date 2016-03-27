package me.tatarka.pokemvvm.pokemon;

import android.support.annotation.ColorInt;

public class Chip {
    @ColorInt
    private final int color;
    private final CharSequence text;

    public Chip(int color, CharSequence text) {
        this.color = color;
        this.text = text;
    }

    public int color() {
        return color;
    }
   
    public CharSequence text() {
        return text;
    }
}
