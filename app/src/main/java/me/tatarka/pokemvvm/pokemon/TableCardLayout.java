package me.tatarka.pokemvvm.pokemon;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collection;

import me.tatarka.pokemvvm.R;

public class TableCardLayout extends CardView {

    private GridLayout gridLayout;
    private TextView titleView;

    public TableCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        int leftRightPadding = getResources().getDimensionPixelOffset(R.dimen.padding_normal);
        int topBottomPadding = getResources().getDimensionPixelOffset(R.dimen.padding_three_halves);
        setContentPadding(leftRightPadding, topBottomPadding, leftRightPadding, topBottomPadding);

        gridLayout = (GridLayout) LayoutInflater.from(getContext()).inflate(R.layout.pokemon_table_card, this, false);
        titleView = (TextView) gridLayout.findViewById(R.id.title);
        addView(gridLayout);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableCardLayout);
            String title = a.getString(R.styleable.TableCardLayout_android_title);
            if (title != null) {
                titleView.setText(title);
            }
            a.recycle();
        }

        if (isInEditMode()) {
            setData(Arrays.asList(
                    new Row("speed", "45"),
                    new Row("special-defense", "65"),
                    new Row("special-attack", "65"),
                    new Row("defense", "49"),
                    new Row("attack", "49"),
                    new Row("hp", "45")
            ));
        }
    }

    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

    public void setData(Collection<Row> rows) {
        gridLayout.removeViews(1, gridLayout.getChildCount() - 1);
        for (Row row : rows) {
            {
                TextView nameView = new TextView(getContext());
                TextViewCompat.setTextAppearance(nameView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Body1);
                nameView.setText(row.name());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                gridLayout.addView(nameView, params);
            }
            {
                TextView valueView = new TextView(getContext());
                TextViewCompat.setTextAppearance(valueView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Body1);
                valueView.setText(row.value());
                gridLayout.addView(valueView);
            }
        }
    }
}
