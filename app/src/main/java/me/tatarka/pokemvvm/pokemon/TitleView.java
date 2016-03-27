package me.tatarka.pokemvvm.pokemon;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import me.tatarka.pokemvvm.R;
import me.tatarka.pokemvvm.dagger.Dagger;
import okhttp3.HttpUrl;

/**
 * Shows a title and a left image, animating the title over to make room for the left image when
 * it's added.
 */
public class TitleView extends FrameLayout {

    @Inject
    Picasso picasso;
    private ViewGroup titleGroup;
    private TextView titleView;
    private TextView subtitleView;
    private ImageView spriteView;
    private int leftPadding;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            Dagger.component(getContext()).pokemon().inject(this);
        }

        inflate(getContext(), R.layout.pokemon_titleview, this);
        titleGroup = (ViewGroup) findViewById(R.id.title_group);
        titleView = (TextView) findViewById(R.id.title);
        subtitleView = (TextView) findViewById(R.id.subtitle);
        spriteView = (ImageView) findViewById(R.id.sprite);
        leftPadding = getResources().getDimensionPixelOffset(R.dimen.padding_normal);

        if (isInEditMode()) {
            titleGroup.setPadding((int) (getResources().getDisplayMetrics().density * 96), 0, 0, 0);
            titleView.setText("#1 Bulbasaur");
            subtitleView.setText("HT: 7 WT: 69");
            spriteView.setImageResource(R.drawable.bulbasaur);
        } else {
            titleGroup.setTranslationX(leftPadding);
        }
    }

    public void setTitle(@Nullable CharSequence title) {
        titleView.setText(title);
    }

    public void setSubtitle(@Nullable CharSequence subtitle) {
        subtitleView.setText(subtitle);
        if (ViewCompat.isLaidOut(this)) {
            subtitleView.setAlpha(0);
            subtitleView.animate()
                    .alpha(1)
                    .setDuration(300);
        }
    }

    public void setImage(@Nullable final HttpUrl image) {
        if (image == null) {
            spriteView.setImageBitmap(null);
            if (ViewCompat.isLaidOut(this)) {
                titleGroup.animate()
                        .translationX(leftPadding)
                        .setDuration(300)
                        .setInterpolator(new AccelerateDecelerateInterpolator());
            } else {
                titleGroup.setTranslationX(leftPadding);
            }
        } else {
            if (ViewCompat.isLaidOut(this)) {
                titleGroup.animate()
                        .translationX(spriteView.getWidth())
                        .setDuration(300)
                        .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            boolean hasLoaded = false;

                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (!hasLoaded && animation.getAnimatedFraction() >= 0.5) {
                                    hasLoaded = true;
                                    picasso.load(image.toString())
                                            .into(spriteView);
                                }
                            }
                        })
                        .setInterpolator(new AccelerateDecelerateInterpolator());
            } else {
                picasso.load(image.toString())
                        .into(spriteView);
                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        titleGroup.setTranslationX(spriteView.getWidth());
                        return false;
                    }
                });
            }
        }
    }
}
